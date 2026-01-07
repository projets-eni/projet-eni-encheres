document.addEventListener('DOMContentLoaded', function() {
    function toggleRecherche() {
        const achatChecked = document.getElementById('achat').checked;
        const venteChecked = document.getElementById('vente').checked;
        const ventesCheckboxes = ['mesVentesEnCours', 'mesVentesNonDebutees', 'mesVentesTerminees'];
        const achatsCheckboxes = ['encheresOuvertes', 'mesEncheres', 'mesEncheresRemportees'];

        // Si ACHAT est sélectionné : désactive ET décoche TOUTES les ventes
        if (achatChecked) {
            ventesCheckboxes.forEach(id => {
                const cb = document.getElementById(id);
                if (cb) {
                    cb.disabled = true;
                    cb.checked = false;  // DÉCOCHe systématiquement
                }
            });
        }
        // Si VENTE est sélectionné : désactive ET décoche TOUS les achats
        if (venteChecked) {
            achatsCheckboxes.forEach(id => {
                const cb = document.getElementById(id);
                if (cb) {
                    cb.disabled = true;
                    cb.checked = false;  // DÉCOCHe systématiquement
                }
            });
        }

        // Active les checkboxes du groupe sélectionné (sans les cocher forcement)
        if (achatChecked) {
            achatsCheckboxes.forEach(id => {
                const cb = document.getElementById(id);
                if (cb) cb.disabled = false;  // Active seulement
            });
        }

        if (venteChecked) {
            ventesCheckboxes.forEach(id => {
                const cb = document.getElementById(id);
                if (cb) cb.disabled = false;  // Active seulement
            });
        }
    }

    document.getElementById('achat').addEventListener('change', toggleRecherche);
    document.getElementById('vente').addEventListener('change', toggleRecherche);

    toggleRecherche(); // Initialisation
});