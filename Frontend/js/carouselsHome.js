"use strict";

const api = "http://localhost:8080/products/";

// Select carousels container
let carouselsdiv = document.querySelector(".all-carousels");

setAllCarousels();

// Create all carousels and add them to carousels div
async function setAllCarousels() {
    let categories = [];
    carouselsdiv.innerHTML = "";
    try {
        let response = await fetch(api + "categories/");
        if (!response.ok) {
            console.log("No se pudo traer la respuesta");
        }

        let allData = await response.json();

        for (let category of allData) {
            categories.push(category);
            carouselsdiv.innerHTML += `
                <div class="carousel">
                    <button data-category="${category}" class="btn-carousel-left"><img src="files/left-arrow.png"></button>
                    <button data-category="${category}" class="btn-carousel-right"><img src="files/right-arrow.png"></button>
                    <div class="cards-section">
                        <h2>Los 5 más vendidos en ${category}</h2>
                        <div class="cards" id="${category}">
                        </div>
                    </div>
                </div>
            `;
        }

        await showCardsByCategory(categories);


    } catch (e) {
        console.log("Error de red");
    }

    await addToCartOfUser();
    await buyProduct();
}

// Add product to cart of user
async function addToCart(idProduct, username) {
    let idProductData = Number(idProduct);
    let urlCart = "http://localhost:8080/cart/add";

    let userIdData = await getUserId(username);

    let data = {
        product: {
            productId: idProductData
        },
        user: {
            userId: userIdData
        },
        amount: 1
    };

    try {
        let response = await fetch(urlCart, {
            method: "POST",
            headers: {
                "Content-type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            let error = await response.text();
            console.log(error);
            return;
        }

        console.log("añadido al carrito exitosamente");

    } catch (e) {
        console.log("error: " + e);
    }
}

async function deleteFromCart(idProduct, username) {
    let idProductData = Number(idProduct);

    let userIdData = await getUserId(username);

    let cartId = await getCartId(idProductData, userIdData);

    let urlDelete = "http://localhost:8080/cart/delete/" + cartId;

    try {
        let response = await fetch(urlDelete, {
            "method": "DELETE",
            "headers": {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.log("no se pudo eliminar el item con el id: " + cartId);
            return;
        }

        console.log("item eliminado correctamente");

    } catch (e) {
        console.log("error de red");
    }
}

// Load cards in every carousel
async function showCardsByCategory(categories) {
    let i = 0;
    for (i = 0; i < categories.length; i++) {
        let page = 0;
        let cardsDiv = document.querySelector(`#${categories[i]}`);

        await loadCards(categories[i], page);

        // cards -> cardSection -> carousel (of current cards)
        let buttonsContainer = cardsDiv.parentElement.parentElement;

        let leftButton = buttonsContainer.querySelector(".btn-carousel-left");
        let rightButton = buttonsContainer.querySelector(".btn-carousel-right");

        leftButton.addEventListener("click", async (event) => {
            const category = event.currentTarget.getAttribute('data-category');
            if (page > 0) {
                page--;
                await loadCards(category, page, 'left');
            }

            await addToCartOfUser();
            await buyProduct();
        });

        rightButton.addEventListener("click", async (event) => {
            const category = event.currentTarget.getAttribute('data-category');
            page++;
            const hasMore = await loadCards(category, page, 'right');

            if (!hasMore) {
                page--;
            }

            await addToCartOfUser();
            await buyProduct();
        });
    }
}

// Create all cards in carousels and handle pagination
async function loadCards(category, page, direction) {
    let cardsContent = document.querySelector(`#${category}`);
    console.log(page);
    try {
        let response = await fetch(api + `filterByCategoryPagination?category=${category}&limit=5&offset=${page}`);

        if (!response.ok) {
            console.log("Error al cargar los productos");
            return false;
        }

        let products = await response.json();

        if (products.length === 0 || products.length < 5) {
            return false;
        }

        cardsContent.innerHTML = "";
        products.forEach(product => {
            cardsContent.innerHTML += `
                <div class="card" id="${product.productId}">
                    <div class="card-image">
                        <img src="/files/add-product.png">
                    </div>
                    <div class="card-product-name">
                        <h3>${product.productName}</h3>
                    </div>
                    <div class="card-price-amount-product">
                        <div class="price-card">
                            <p>$${product.price}</p>
                        </div>
                    </div>
                    <div class="card-buttons">
                        <div class="add-to-cart-btn">
                            <button class="cart-button" name="${product.productId}"><img src="/files/shopping-cart-card.png"></button>
                        </div>
                        <div class="buy-btn">
                            <button class="buy-button" name="${product.productId}">Comprar</button>
                        </div>
                    </div>
                </div>
            `;
        });

        return true;

    } catch (e) {
        console.log("Error de red: " + e);
        return false;
    }
}

// Get user id from username
async function getUserId(username) {
    let urlUsername = "http://localhost:8080/auth/getUserId/" + username;

    let response = await fetch(urlUsername, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        console.log("error al obtener userId");
        return;
    }

    let userId = await response.json();

    return userId;
}

//Get cart id
async function getCartId(productId, userId) {
    let urlCartId = "http://localhost:8080/cart/getCartIdFromUser/" + userId + "/" + productId;

    let response = await fetch(urlCartId, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        console.log("error al obtener userId");
        return;
    }

    let cartId = await response.json();

    return cartId;
}

//add product to user cart
async function addToCartOfUser() {
    let addToCartsButtons = document.querySelectorAll(".cart-button");

    addToCartsButtons.forEach(cartButton => {
        cartButton.addEventListener("click", async () => {
            if (!token) {
                location.href = "/pages/login.html";
                return;
            } else {
                const decodedToken = parseJwt(token);

                let username = decodedToken.sub;
                let idProduct = cartButton.getAttribute("name");
                let imgCart = cartButton.firstElementChild;

                if (imgCart.src.includes("/files/shopping-cart-card.png")) {
                    imgCart.src = "/files/added-to-cart.png";
                    await addToCart(idProduct, username);
                } else if (imgCart.src.includes("/files/added-to-cart.png")) {
                    imgCart.src = "/files/shopping-cart-card.png";
                    await deleteFromCart(idProduct, username);
                }
            }
        });
    });
}

//buy product from card
async function buyProduct() {
    let buyProductsButtons = document.querySelectorAll(".buy-button");

    buyProductsButtons.forEach(buyButton => {
        buyButton.addEventListener("click", async () => {
            if (!token) {
                location.href = "/pages/login.html";
                return;
            } else {
                if (localStorage.getItem("allProductsInCart") != null) {
                    localStorage.removeItem("allProductsInCart");
                }

                const decodedToken = parseJwt(token);

                let username = decodedToken.sub;
                let idProduct = buyButton.getAttribute("name");
                let imgProduct = buyButton.firstElementChild;

                localStorage.setItem("idProduct", idProduct);
                localStorage.setItem("imgCart", imgProduct);

                location.href = "/pages/purchaseform.html";
            }
        });
    });
}
