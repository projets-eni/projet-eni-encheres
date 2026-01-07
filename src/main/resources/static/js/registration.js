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

        // État de validation
        const inputsValidity = {
            pseudo: false, nom: false, prenom: false, email: false,
            telephone: false, rue: false, codePostal: false, ville: false,
            motDePasse: false, confirmationMotDePasse: false
        };

        // Fonction pour masquer TOUS les messages d'erreur au chargement
        const hideAllErrorMessages = () => {
            document.querySelectorAll('#inscription-container .error-msg').forEach(span => {
                span.style.display = 'none';
            });
            document.querySelectorAll('#inscription-container input').forEach(input => {
                input.classList.remove('red-border', 'green-border');
            });
        };

        // Récupérer le span d'erreur associé à l'input
        const getErrorSpan = (input) => input.parentElement.querySelector(".error-msg");

        // Fonction showValidation EXACTEMENT comme vous l'aviez
        const showValidation = ({input, validation}) => {
            const errorSpan = getErrorSpan(input);

            if (!input.value) {
                errorSpan.style.display = "block";
                input.classList.remove("green-border");
                input.classList.add("red-border");
                return false;
            }

            if (validation) {
                errorSpan.style.display = "none";
                input.classList.remove("red-border");
                input.classList.add("green-border");
                return true;
            } else {
                errorSpan.style.display = "block";
                input.classList.remove("green-border");
                input.classList.add("red-border");
                return false;
            }
        };

        // Vérification globale du formulaire
        const checkAllInputs = () => {
            const isFormValid = Object.values(inputsValidity).every(valid => valid);
            submitButton.disabled = !isFormValid;
            if (isFormValid) {
                submitButton.classList.remove("disabled");
            } else {
                submitButton.classList.add("disabled");
            }
        };

        // VALIDATIONS INDIVIDUELLES (utilisant showValidation({input, validation}))
        const pseudoCheck = () => {
            const regexPseudo = /^[a-zA-Z0-9]+$/;
            const pseudo = pseudoInput.value.trim();
            const isValid = regexPseudo.test(pseudo) && pseudo.length >= 2;
            inputsValidity.pseudo = showValidation({input: pseudoInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.pseudo;
        };

        const nomCheck = () => {
            const isValid = nomInput.value.trim().length >= 2;
            inputsValidity.nom = showValidation({input: nomInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.nom;
        };

        const prenomCheck = () => {
            const isValid = prenomInput.value.trim().length >= 2;
            inputsValidity.prenom = showValidation({input: prenomInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.prenom;
        };

        const emailCheck = () => {
            const regexEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
            const isValid = regexEmail.test(emailInput.value.trim());
            inputsValidity.email = showValidation({input: emailInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.email;
        };

        const telephoneCheck = () => {
            const regexTelephone = /^[0-9]+$/;
            const isValid = regexTelephone.test(telephoneInput.value.trim());
            inputsValidity.telephone = showValidation({input: telephoneInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.telephone;
        };

        const rueCheck = () => {
            const isValid = rueInput.value.trim().length >= 2;
            inputsValidity.rue = showValidation({input: rueInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.rue;
        };

        const codePostalCheck = () => {
            const isValid = codePostalInput.value.trim().length >= 2;
            inputsValidity.codePostal = showValidation({input: codePostalInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.codePostal;
        };

        const villeCheck = () => {
            const isValid = villeInput.value.trim().length >= 2;
            inputsValidity.ville = showValidation({input: villeInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.ville;
        };

        const motDePasseCheck = () => {
            const regexMotDePasse = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&,.])[A-Za-z\d@$!%*#?&,.]{8,}$/;
            const isValid = regexMotDePasse.test(motDePasseInput.value);
            inputsValidity.motDePasse = showValidation({input: motDePasseInput, validation: isValid});
            checkAllInputs();
            return inputsValidity.motDePasse;
        };

        const confirmationMotDePasseCheck = () => {
            const motDePasseValue = motDePasseInput.value;
            const confirmationValue = confirmationMotDePasseInput.value;

            if (!confirmationValue) {
                const errorSpan = getErrorSpan(confirmationMotDePasseInput);
                errorSpan.style.display = "none";
                confirmationMotDePasseInput.classList.remove("red-border", "green-border");
                inputsValidity.confirmationMotDePasse = false;
            } else {
                const isValid = confirmationValue === motDePasseValue && motDePasseValue !== "";
                inputsValidity.confirmationMotDePasse = showValidation({input: confirmationMotDePasseInput, validation: isValid});
            }
            checkAllInputs();
            return inputsValidity.confirmationMotDePasse;
        };

        // Événements pour chaque champ
        [pseudoInput, nomInput, prenomInput, emailInput, telephoneInput, rueInput,
            codePostalInput, villeInput, motDePasseInput, confirmationMotDePasseInput].forEach(input => {
            if (input) {
                input.addEventListener("input", () => {
                    switch(input.id) {
                        case "pseudo": pseudoCheck(); break;
                        case "nom": nomCheck(); break;
                        case "prenom": prenomCheck(); break;
                        case "email": emailCheck(); break;
                        case "telephone": telephoneCheck(); break;
                        case "rue": rueCheck(); break;
                        case "codePostal": codePostalCheck(); break;
                        case "ville": villeCheck(); break;
                        case "motDePasse": motDePasseCheck(); break;
                        case "confirmationMotDePasse": confirmationMotDePasseCheck(); break;
                    }
                });

                input.addEventListener("blur", () => {
                    switch(input.id) {
                        case "pseudo": pseudoCheck(); break;
                        case "nom": nomCheck(); break;
                        case "prenom": prenomCheck(); break;
                        case "email": emailCheck(); break;
                        case "telephone": telephoneCheck(); break;
                        case "rue": rueCheck(); break;
                        case "codePostal": codePostalCheck(); break;
                        case "ville": villeCheck(); break;
                        case "motDePasse": motDePasseCheck(); break;
                        case "confirmationMotDePasse": confirmationMotDePasseCheck(); break;
                    }
                });
            }
        });

        // SUBMISSION DU FORMULAIRE
        const handleForm = function (e) {
            const isFormValid = Object.values(inputsValidity).every(valid => valid);

            if (!isFormValid) {
                e.preventDefault();
                alert("Veuillez corriger les erreurs dans le formulaire.");
                return false;
            }
            console.log("✅ Formulaire soumis avec succès !");
            return true;
        };

        registrationForm.addEventListener("submit", handleForm);

        // Initialisation
        hideAllErrorMessages();
        submitButton.disabled = true;
        submitButton.classList.add("disabled");
    }
});