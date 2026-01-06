document.addEventListener('DOMContentLoaded', function() {
    function toggleRecherche() {
        const achatChecked = document.getElementById('achat').checked;
        const venteChecked = document.getElementById('vente').checked;
        const ventesCheckboxes = ['mesVentesEnCours', 'mesVentesNonDebutees', 'mesVentesTerminees'];
        const achatsCheckboxes = ['encheresOuvertes', 'mesEncheres', 'mesEncheresRemportees'];

        // BIDIRECTIONNEL : désactive si achat, active si vente
        ventesCheckboxes.forEach(id => {
            const cb = document.getElementById(id);
            if (cb) {
                cb.disabled = achatChecked;  // désactive si ACHATS est coché, et active si ACHATS n'est pas coché
                cb.checked = venteChecked;  // coche si VENTES est sélectionné
            }
        });

        // BIDIRECTIONNEL : désactive si vente, active si achat
        achatsCheckboxes.forEach(id => {
            const cb = document.getElementById(id);
            if (cb) {
                cb.disabled = venteChecked;  // désactive si VENTES est coché, et active si VENTES n'est pas coché
                cb.checked = achatChecked;  // coche si ACHATS est sélectionné
            }
        });
    }

    // Écouteurs SUR LES DEUX radios
    document.getElementById('achat').addEventListener('change', toggleRecherche);
    document.getElementById('vente').addEventListener('change', toggleRecherche);

    toggleRecherche();

});