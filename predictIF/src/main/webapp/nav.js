const NAV = {
    "": [
        '<li><a href="index.html">Accueil</a></li>',
        '<li><a href="mediums.html">Liste des médiums</a></li>',
        '<li><a href="connexion.html">Connexion</a></li>',
        '<li><a href="inscription.html">Inscription</a></li>'
    ],
    "client": [
        '<li><a href="espaceClient.html">Accueil</a></li>',
        '<li><a href="mediums.html">Liste des médiums</a></li>',
        '<li><a href="historiqueClient.html">Historique</a></li>',
        '<li><a href="profilAstral.html">Profil Astral</a></li>',
        '<li><a href="#" class="deconnexion" id="btnDeconnexion">Déconnexion</a></li>'
    ],
    "employe": [
        '<li><a href="espaceEmploye.html">Accueil</a></li>',
        '<li><a href="mediums.html">Liste des médiums</a></li>',
        '<li><a href="maConsultation.html">Ma Consultation</a></li>',
        '<li><a href="carteClients.html">Carte Clients</a></li>',
        '<li><a href="dashboard.html">Dashboard</a></li>',
        '<li><a href="#" class="deconnexion" id="btnDeconnexion">Déconnexion</a></li>'
    ]
};

let _profilPromise = null;
function getProfil() {
    if (!_profilPromise) {
        _profilPromise = fetch("/predictIF/ActionServlet?todo=session")
            .then(r => r.json())
            .then(d => d.profil || "")
            .catch(() => "");
    }
    return _profilPromise;
}

function chargerFooter() {
    const footer = document.createElement("footer");
    footer.innerHTML =
        '<div class="footer-main">'
      +   '<div class="footer-bloc">'
      +     '<h3>Predict\'IF</h3>'
      +     '<p>Insa Lyon<br>31 avenue Jean Capelle<br>69621 Villeurbanne</p>'
      +   '</div>'
      +   '<div class="footer-bloc">'
      +     '<h3>Contact</h3>'
      +     '<p>contact@predictif.fr<br>06 83 77 44 55</p>'
      +   '</div>'
      + '</div>'
      + '<div class="footer-bas">© 2026 Predict\'IF – Projet INSA Lyon. Tous droits réservés.</div>';
    document.body.appendChild(footer);
}

async function chargerNav() {
    try {
        const profil = await getProfil();
        document.getElementById("nav-liens").innerHTML = (NAV[profil] || NAV[""]).join("");
        const btn = document.getElementById("btnDeconnexion");
        if (btn) btn.addEventListener("click", function(e) {
            e.preventDefault();
            fetch("/predictIF/ActionServlet?todo=deconnexion")
                .then(() => { window.location.href = "index.html"; });
        });
    } catch(e) {
        document.getElementById("nav-liens").innerHTML = NAV[""].join("");
    }
}
