"use strict";

let url;


if(location.pathname.includes("login")) {
    url = "http://localhost:8080/auth/login";
}
else if(location.pathname.includes("register")) {
    url = "http://localhost:8080/auth/register";
}

let formLogin = document.querySelector("#formLogin");
let loginMessage = document.querySelector("#login-message");
let formRegister = document.querySelector("#formRegister");
let registerMessage = document.querySelector("#register-message");

if (formLogin) {
    formLogin.addEventListener("submit", login);
}

if(formRegister) {
    formRegister.addEventListener("submit", register);
}

async function register(e) {
    e.preventDefault();
    registerMessage.innerHTML = "";
    let formData = new FormData(formRegister);

    let username = formData.get('username');
    let pass1 = formData.get('pass1');
    let pass2 = formData.get('pass2');
    let email = formData.get('email');

    let data = {
        "username": username,
        "password": pass1,
        "email": email
    }

    if(pass1 !== pass2) {
        registerMessage.innerHTML = "Las contraseñas no coinciden";
    }
    else {
        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    'Content-type': 'application/json'
                },
                body: JSON.stringify(data),
            });

            const responseData = await response.json();
            const token = responseData.token;

            localStorage.setItem('token', token);

            if(!response.ok) {
                registerMessage.innerHTML = "El username o la contraseña son incorrectos";
            }
            else {
                registerMessage.innerHTML = "El usuario se registró correctamente.";

                setTimeout(() => {
                    window.location.href = "../index.html"
                }, 800);

            }

        }
        catch(error) {
            console.log(error);
        }
    }



}

async function login(e) {
    e.preventDefault();
    loginMessage.innerHTML = "";
    let formData = new FormData(formLogin);

    let username = formData.get('username');
    let pass = formData.get('pass');

    let data = {
        "username": username,
        "password": pass
    }


    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data),
        });

        if (response.status === 403) {
            loginMessage.innerHTML = "El username o la contraseña son incorrectos";
            return;  // Termina la ejecución si la respuesta no es correcta
        }

        console.log("localstorage antes de borrar token anterior"+ localStorage.getItem("token"));
        localStorage.removeItem("token");
        const responseData = await response.json();
        const token = responseData.token;
        localStorage.setItem('token', token);
        console.log("localstorage despues de borrar token anterior"+ localStorage.getItem("token"));

        loginMessage.innerHTML = "El usuario se ingresó correctamente.";

        setTimeout(() => {
            window.location.href = "../index.html";
        }, 800);

    } catch (error) {
        console.log(error);
    }

  
       
}