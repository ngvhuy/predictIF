const NAV = {
    "": [
        '<li><a href="index.html">Accueil</a></li>',
        '<li><a href="mediums.html">Liste des médiums</a></li>',
        '<li><a href="connexion.html">Connexion</a></li>',
        '<li><a href="inscription.html">Inscription</a></li>'
    ],
    "client": [
        '<li><a href="index.html">Accueil</a></li>',
        '<li><a href="mediums.html">Liste des médiums</a></li>',
        '<li><a href="historiqueClient.html">Historique</a></li>',
        '<li><a href="profilAstral.html">Profil Astral</a></li>',
        '<li><a href="#" class="deconnexion" id="btnDeconnexion">Déconnexion</a></li>'
    ],
    "employe": [
        '<li><a href="index.html">Accueil</a></li>',
        '<li><a href="mediums.html">Liste des médiums</a></li>',
        '<li><a href="maConsultation.html">Ma Consultation</a></li>',
        '<li><a href="carteClients.html">Carte Clients</a></li>',
        '<li><a href="graphique.html">Graphique</a></li>',
        '<li><a href="#" class="deconnexion" id="btnDeconnexion">Déconnexion</a></li>'
    ]
};

async function chargerNav() {
    try {
        const res = await fetch("/predictIF/ActionServlet?todo=session");
        const data = await res.json();
        document.getElementById("nav-liens").innerHTML = (NAV[data.profil || ""] || NAV[""]).join("");
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
