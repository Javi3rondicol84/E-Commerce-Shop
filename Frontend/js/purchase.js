"use strict";

if(!token) {
    location.href = "/index.html";
}

let idProduct = localStorage.getItem("idProduct");
let userId = localStorage.getItem("userId");

let urlPurchase = "";

if(idProduct != null) {
    urlPurchase = "http://localhost:8080/products/"+idProduct;
    showOneProductPurchase();
}
else {
    urlPurchase = "http://localhost:8080/cart/cleancart/"+userId;
    showProductsPurchase();
}

async function showOneProductPurchase() {

    let productsPurchaseItems = document.querySelector(".productsPurchaseItems");
    let amountProducts = document.querySelector("#amountProducts");
    let totalPurchase = document.querySelector("#totalPurchase");
    let confirmPurchaseButton = document.querySelector("#confirmPurchase");

    try {
        let response = await fetch(urlPurchase);

        if(response.ok) {

            let dataProduct = await response.json();

                productsPurchaseItems.innerHTML += `
                    <h2>Nombre: ${dataProduct.productName}</h2>
                    <h3>Descripcion: </h3>
                    <p>${dataProduct.description}</p>
                `;

            amountProducts.innerHTML = "Total: "+1;
            totalPurchase.innerHTML =  "Precio: "+dataProduct.price;

            confirmPurchaseButton.addEventListener("click", (e) => {
                e.preventDefault();
                localStorage.removeItem("idProduct");
                location.href = "/index.html";
            });

        }
        else {
            console.log("la respuesta falló");
        }



    }
    catch(e) {
        console.log("error de red" + e);
    }

}

async function showProductsPurchase() {
    let productsPurchaseItems = document.querySelector(".productsPurchaseItems");
    let amountProducts = document.querySelector("#amountProducts");
    let totalPurchase = document.querySelector("#totalPurchase");
    let confirmPurchaseButton = document.querySelector("#confirmPurchase");

    let totalPrice = 0;
    let amountProductsSize = 0;

    let allProductsInCart = JSON.parse(localStorage.getItem("allProductsInCart"));
    console.log(allProductsInCart);
    for(let item of allProductsInCart) {

        productsPurchaseItems.innerHTML += `
            <h2>Nombre: ${item.product.productName}</h2>
            <h3>Descripcion: </h3>
            <p>${item.product.description}</p>
        `;

        amountProductsSize += item.amount;
        totalPrice += item.product.price;

    }

    let totalPriceRounded = totalPrice.toFixed(1);

    amountProducts.innerHTML = "Total: "+amountProductsSize;
    totalPurchase.innerHTML =  "Precio: $"+totalPriceRounded;

    confirmPurchaseButton.addEventListener("click", async (e) => {
        e.preventDefault();
        localStorage.removeItem("allProductsInCart");
        localStorage.removeItem("userId");
        await cleanCart();
        location.href = "/index.html";
    });

}

async function cleanCart() {

    try {
        let response = await fetch(urlPurchase, {
           method: "DELETE",
           headers: {
               "Content-type": "application/json",
               "Authorization": `Bearer ${token}`
           }
        });

        if(response.ok) {
            console.log("carrito vaciado exitosamente");
        }
        else {
            console.log("error en la respuesta");
        }

    }
    catch(e) {
        console.log("error de red "+e);
    }

}