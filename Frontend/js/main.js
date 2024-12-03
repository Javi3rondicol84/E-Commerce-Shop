"use strict";

const token = localStorage.getItem('token');
const apiMenu = "http://localhost:8080/products/";

// Search products section
document.querySelector("#searchButton").addEventListener("click", searchResult);

async function searchResult(e) {
    e.preventDefault();

    let searchValue = document.querySelector("#searchValue").value;
    const searchURL = `/pages/result.html?search=${encodeURIComponent(searchValue)}`;

    if (window.location.pathname.includes('result.html') || window.location.pathname.includes('cart.html')) {
        window.location.href = searchURL;
    } else {
        window.location.href = searchURL;
    }
}

// Anchor login - register in nav
let loginRegisterDiv = document.querySelector(".nav-first-second");
main();

async function main() {
    // If user is logged
    if (token) {
        loginRegisterDiv.innerHTML = "";
        const decodedToken = parseJwt(token);
        const currentTime = Math.floor(Date.now() / 1000);

        if (decodedToken.exp > currentTime) {
            loginRegisterDiv.innerHTML = `
                <ul>
                    <li class="closeSession">Cerrar sesión [${decodedToken.sub}:${decodedToken.authorities}]</li>
                </ul>`;

            let closeSessionLi = document.querySelector(".closeSession");
            closeSessionLi.addEventListener("click", () => {
                localStorage.removeItem('token');
                location.reload();
            });
        } else {
            // Token has expired
            localStorage.removeItem('token');
            loginRegisterDiv.innerHTML = `
                <ul>
                    <li><a href="/pages/login.html">Iniciar sesión</a></li>
                    <li><a href="/pages/register.html">Registrarse</a></li>
                </ul>`;
        }
    } else {
        loginRegisterDiv.innerHTML = `
            <ul>
                <li><a href="/pages/login.html">Iniciar sesión</a></li>
                <li><a href="/pages/register.html">Registrarse</a></li>
            </ul>`;
    }
}

// Decode JWT Token
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("Token no válido o malformado", e);
        return null;
    }
}

/* Dropdown menu section */
let dropdownMenuLi = document.querySelector(".dropdownLiButton");
let dropdownMenu = document.querySelector(".categoriesMenuDropdown");

dropdownMenuLi.addEventListener("mouseenter", function() {
    dropdownMenu.classList.toggle("hidden");

    let ulCategories = document.querySelector(".categoriesLi");
    if (ulCategories.querySelectorAll('li').length === 0) {
        addCategoriesLi(ulCategories);
    }
});

dropdownMenu.addEventListener("mouseleave", function() {
    dropdownMenu.classList.toggle("hidden");
});

async function addCategoriesLi(ulCategories) {
    try {
        let response = await fetch(apiMenu + "categories/");

        if (!response.ok) {
            console.log("Error: bad request");
            return;
        }

        let categories = await response.json();
        categories.forEach(category => {
            ulCategories.innerHTML += `
                <a href="/pages/result.html?category=${category}"><li>${category}</li></a>`;
        });
    } catch (e) {
        console.log(e);
    }
}
