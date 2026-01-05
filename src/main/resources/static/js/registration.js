

document.addEventListener('DOMContentLoaded', function() {
    const registrationForm = document.getElementById('inscription-container');
    if (registrationForm) {
        const pseudoInput = document.getElementById("pseudo");
        const nomInput = document.getElementById("nom");
        const prenomInput = document.getElementById("prenom");
        const emailInput = document.getElementById("email");
        const telephoneInput = document.getElementById("telephone");
        const rueInput = document.getElementById("rue");
        const codePostalInput = document.getElementById("codePostal");
        const villeInput = document.getElementById("ville");
        const motDePasseInput = document.getElementById("motDePasse");
        const confirmationMotDePasseInput = document.getElementById("confirmationMotDePasse");
        const submitButton = document.getElementById("submit-button");

        // Fonction pour masquer TOUS les messages d'erreur au chargement
        const hideAllErrorMessages = () => {
            document.querySelectorAll('#inscription-container .error-msg').forEach(span => {
                span.style.display = 'none';
            });
            // Reset des bordures
            document.querySelectorAll('#inscription-container input').forEach(input => {
                input.classList.remove('red-border', 'green-border');
            });
        };

        const inputsValidity = {
            pseudo: false,
            nom: false,
            prenom: false,
            email: false,
            telephone: false,
            rue: false,
            codePostal: false,
            ville: false,
            motDePasse: false,
            confirmationMotDePasse: false
        }
        // Fonction pour récupérer le span d'erreur associé à l'input
        const getErrorSpan = (input) => input.parentElement.querySelector(".error-msg");

        const showValidation = ({input, validation}) => {

            const errorSpan = getErrorSpan(input);

            if (!input.value) {
                errorSpan.style.display = "block";
                input.classList.remove("green-border");
                input.classList.add("red-border");
                return;
            }

            if (validation) {
                errorSpan.style.display = "none";
                input.classList.remove("red-border");
                input.classList.add("green-border");
            } else {
                errorSpan.style.display = "block";
                input.classList.remove("green-border");
                input.classList.add("red-border");
            }
        };

        const pseudoCheck = () => {
            if (pseudoInput.value.length >= 2) {
                showValidation({input: pseudoInput, validation: true});
                inputsValidity.pseudo = true;
            } else {
                showValidation({input: pseudoInput, validation: false});
                inputsValidity.pseudo = false;
            }
            return inputsValidity.pseudo;
        };

        const nomCheck = () => {
            if (nomInput.value.length >= 2) {
                showValidation({input: nomInput, validation: true});
                inputsValidity.nom = true;
            } else {
                showValidation({input: nomInput, validation: false});
                inputsValidity.nom = false;
            }
        };

        const prenomCheck = () => {
            if (prenomInput.value.length >= 2) {
                showValidation({input: prenomInput, validation: true});
                inputsValidity.prenom = true;
            } else {
                showValidation({input: prenomInput, validation: false});
                inputsValidity.prenom = false;
            }
        };

        const emailCheck = () => {
            const regexEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
            if (regexEmail.test(emailInput.value.trim())) {
                showValidation({input: emailInput, validation: true});
                inputsValidity.email = true;
            } else {
                showValidation({input: emailInput, validation: false});
                inputsValidity.email = false;
            }
        };

        const telephoneCheck = () => {
            const regexTelephone = /^[0-9]+$/;
            if (regexTelephone.test(telephoneInput.value.trim())) {
                showValidation({input: telephoneInput, validation: true});
                inputsValidity.telephone = true;
            } else {
                showValidation({input: telephoneInput, validation: false});
                inputsValidity.telephone = false;
            }
        };

        const rueCheck = () => {
            if (rueInput.value.length >= 2) {
                showValidation({input: rueInput, validation: true});
                inputsValidity.rue = true;
            } else {
                showValidation({input: rueInput, validation: false});
                inputsValidity.rue = false;
            }
        };

        const codePostalCheck = () => {
            if (codePostalInput.value.length >= 2) {
                showValidation({input: codePostalInput, validation: true});
                inputsValidity.codePostal = true;
            } else {
                showValidation({input: codePostalInput, validation: false});
                inputsValidity.codePostal = false;
            }
        };

        const villeCheck = () => {
            if (villeInput.value.length >= 2) {
                showValidation({input: villeInput, validation: true});
                inputsValidity.ville = true;
            } else {
                showValidation({input: villeInput, validation: false});
                inputsValidity.ville = false;
            }
        };

        // MOT DE PASSE
        const motDePasseCheck = function () {

            const regexMotDePasse = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&,.])[A-Za-z\d@$!%*#?&,.]{8,}$/;
            if (regexMotDePasse.test(motDePasseInput.value.trim())) {
                showValidation({input: motDePasseInput, validation: true});
                inputsValidity.motDePasse = true;
            } else {
                showValidation({input: motDePasseInput, validation: false});
                inputsValidity.motDePasse = false;
            }
        };

        const confirmationMotDePasseCheck = function () {
            let motDePasseValue = motDePasseInput.value;
            //je récupère la valeur de la confirmation du mot de passe
            let confirmationMotDePasseValue = confirmationMotDePasseInput.value;

            if (!confirmationMotDePasseValue && !motDePasseValue) {
                confirmationMotDePasseInput.classList.remove("red-border", "green-border");
                const errorSpan = getErrorSpan(confirmationMotDePasseInput);
                errorSpan.style.display = "none";
                inputsValidity.confirmationMotDePasse = false;
            } else if (confirmationMotDePasseValue !== motDePasseValue) {
                showValidation({input: confirmationMotDePasseInput, validation: false})
                inputsValidity.confirmationMotDePasse = false;
            } else {
                showValidation({input: confirmationMotDePasseInput, validation: true})
                inputsValidity.confirmationMotDePasse = true;
            }
        }

        pseudoInput.addEventListener("blur", pseudoCheck);
        pseudoInput.addEventListener("input", pseudoCheck);

        nomInput.addEventListener("blur", nomCheck);
        nomInput.addEventListener("input", nomCheck);

        prenomInput.addEventListener("blur", prenomCheck);
        prenomInput.addEventListener("input", prenomCheck);

        emailInput.addEventListener("blur", emailCheck);
        emailInput.addEventListener("input", emailCheck);

        telephoneInput.addEventListener("blur", telephoneCheck);
        telephoneInput.addEventListener("input", telephoneCheck);

        rueInput.addEventListener("blur", rueCheck);
        rueInput.addEventListener("input", rueCheck);

        codePostalInput.addEventListener("blur", codePostalCheck);
        codePostalInput.addEventListener("input", codePostalCheck);

        villeInput.addEventListener("blur", villeCheck);
        villeInput.addEventListener("input", villeCheck);

        motDePasseInput.addEventListener("blur", motDePasseCheck);
        motDePasseInput.addEventListener("input", motDePasseCheck);

        confirmationMotDePasseInput.addEventListener("blur", confirmationMotDePasseCheck);
        confirmationMotDePasseInput.addEventListener("input", confirmationMotDePasseCheck);

        const allInputsCheck = function () {

            if (inputsValidity.pseudo &&
                inputsValidity.nom &&
                inputsValidity.prenom &&
                inputsValidity.email &&
                inputsValidity.telephone &&
                inputsValidity.rue &&
                inputsValidity.codePostal &&
                inputsValidity.ville &&
                inputsValidity.motDePasse &&
                inputsValidity.confirmationMotDePasse
            ) {

                submitButton.disabled = false;
                submitButton.classList.remove("disabled");
            } else {
                submitButton.disabled = true;
                submitButton.classList.add("disabled");
            }
        }
        registrationForm.addEventListener("input", allInputsCheck);

        const handleForm = function (e) {
            if (pseudoCheck() && nomCheck() && prenomCheck() && emailCheck() && telephoneCheck() && rueCheck() && codePostalCheck() && villeCheck() && motDePasseCheck() && confirmationMotDePasseCheck()) {
                alert("Vos données ont été envoyées avec succès !")
            } else {
                e.preventDefault()
            }
        }
        registrationForm.addEventListener("submit", handleForm);
    }
});




