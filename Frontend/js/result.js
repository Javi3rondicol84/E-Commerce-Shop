"use strict";

window.onload = function() {
    let resultsCardsDiv = document.querySelector(".resultsCards");

    showResults();

    //show search results(categories or search input)
    async function showResults() {
        let apiRoute = "http://localhost:8080/products/";
        let urlParams = new URLSearchParams(window.location.search);
        let searchValue;

        if(location.search.includes("search")) {
            apiRoute += "search?keyword=";
            searchValue = urlParams.get('search');
        }
        else if(location.search.includes("category")) {
            apiRoute += "category?category=";
            searchValue = urlParams.get('category');
        }

        resultsCardsDiv.innerHTML = "";

        try {

            let response = await fetch(apiRoute+searchValue);

            if(!response.ok) {
                console.log("fallido, sin productos");
            }

            let products = await response.json();

            products.forEach(product => {

                resultsCardsDiv.innerHTML +=
                    `
                        <div class="resultCard">
                            <img src="../files/add-product.png">
                            <div class="resultCardInfo">
                                <h2>${product.productName}</h2>
                                <p>$${product.price}</p>
                            </div>
                        </div>
                    `
            });

        }
        catch(e) {
            console.log(e);
        }
    }

}