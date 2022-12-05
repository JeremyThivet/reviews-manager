
module.exports = {
    homeguest: {
        title: "Bienvenue sur Revieworld",
        description: "S'il s'agit de votre première visite, vous pouvez dès à présent créer un compte avec le bouton ci-dessus. Sinon, vous pouvez vous connecter en saisissant vos identifiants."
    },
    legalnotice: {
        title: "Mentions légales",
        description: "Conformément aux dispositions de la loi n° 2004-575 du 21 juin 2004 pour la confiance en l’économie numérique, il est précisé aux utilisateurs de Revieworld l’identité des différents intervenants dans le cadre de sa mise en oeuvre.",
        titleHeb: "Hébergeur",
        descrHeb: "L'hébergement du site web est assuré par la société  par la société Amazon Web Services LLC.",
        descr2Heb: "Adresse: P.O. Box 81226, Seattle, WA 98108-122",
        websiteHeb: "https://aws.amazon.com/fr/legal/",
        textLinkHeb: "Plus d'informations",
        titleCookies: "Utilisation des cookies",
        descrCookies: "Ce site web utilise les cookies pour vous proposer une expérience utilisateur unique. Les seules données collectées nous permettent de 1) Retenir votre choix concernant la politique de cookies. 2) Retenir votre choix de préférence de connexion longue (Bouton rester connecté sur l'interface de connexion) et c'est tout :) !"
    },
    register: {
        usernameLabel: "Nom d'utilisateur :",
        usernamePh: "Yandu",
        passLabel: "Mot de passe :",
        passPh: "***********",
        repassLabel: "Confirmation de mot de passe :",
        missingUsername: "^Le nom d'utilisateur est obligatoire",
        sizeUsername: "^Le nom d'utilisateur doit être compris entre 2 et 25 caractères.",
        formatUsername: "^Le nom d'utilisateur doit être composé de lettres et chiffres uniquement. (Au moins une lettre est requise).",
        missingPass: "^Le mot de passe est obligatoire",
        sizePass: "^Le mot de passe doit contenir au moins 8 caractères.",
        formatPass: "^Le mot de passe doit être composé au minimum d'un chiffre et d'une lettre.",
        equalRepass: "^Les deux mots de passe saisis doivent être identiques.",
        missingRepass: "^Veuillez confirmer votre mot de passe saisi ci-dessus.",
        buttonTitle: "S'inscrire",
        congrats: "Félicitations !",
        congratsDesc: "Votre inscription est maintenant terminée, vous pouvez désormais vous connecter.",
        usernameAlreadyExists: "Le nom d'utilisateur choisi existe déjà.",
        generalError: "Une erreur s'est produite côté serveur. Veuillez réessayer."
    },
    firstuserpage: {
        title: "Enregistrement du premier utilisateur",
        description: "Vous êtes le premier utilisateur de cette application. Veuillez vous enregistrer en tant qu'administrateur (ce compte disposera des droits de gestion de l'application entière). Attention, seule le premier utilisateur enregistré à partir de ce formulaire disposera des droits d'administration."
    },
    subscriptionpage: {
        title: "Inscription en tant qu'utilisateur",
        description: "Vous pouvez utiliser le formulaire ci-dessous pour vous inscrire et pouvoir ainsi utiliser l'application."
    },
    navbar: {
        title: "Revieworld",
        connection: "Se connecter",
        register: "S'inscrire",
        home: "Accueil",
        reviews: "Mes classements",
        reviewsManager: "Gestionnaire de classements",
        logoff: "Me déconnecter"
    },
    connection: {
        title: "Connexion",
        description: "Utilisez vos identifiants pour vous connecter. Si vous n'avez pas encore de compte, vous pouvez en créer un en cliquant sur \"S'inscrire\" dans la barre de navigation.",
        buttonTitle: "Se connecter",
        usernameLabel: "Nom d'utilisateur :",
        usernamePh: "Yandu",
        passLabel: "Mot de passe :",
        passPh: "***********",
        success: "Connexion réussie ! Vous allez être redirigé...",
        successTitle: "Authentification réussie",
        stayConnected: "Rester connecté"
    },
    accesscontrol: {
        loginError: "Erreur lors de la connexion, veuillez vérifier vos identifiants."
    },
    footer: {
        allrights: "Tous droits réservés",
        legalNotice: "Mentions légales"
    },
    listsManager: {
        title: "Gestion de vos classements",
        addButton: "Créer un nouveau classement",
        listnameLabel: "Nom de votre nouveau classement :",
        listNamePh: "Mes films visionnés, Mes restaurants visités, ...",
        tabListName: "Nom du classement",
        tabListCreationDate: "Date de création",
        tabListUpdateDate: "Date de dernière modification",
        tabListActions: "Actions",
        successDeletedTitle: "Confirmation de suppression",
        successDeletedContent: "La liste a été supprimée avec succès.",
        failDeletedTitle: "Erreur",
        failDeletedContent: "Une erreur s'est produite lors de la suppression de la liste.",
        errorServer: "Une erreur s'est produite côté serveur, veuillez réessayer."
    },
    lists: {
        title: "Consultation et alimentation de vos classements",
        tabListName: "Nom du classement",
        tabListUpdateDate: "Date de dernière modification",
        tabListNumberEntries: "Nombre d'entrées dans le classement",
        descr: "Vous pouvez retrouver tous vos classements ci-dessous. Pour pouvoir ajouter une nouvelle entrée, cliquez simplement sur le classement cible.",
        descr2: "Si vous souhaitez créer un nouveau classement, ou éditer un classement existant (ajouter un champ personnalisé, ...), dirigez-vous vers le gestionnaire de classement grâce à la barre de navigation ci-dessus."
    },
    editList: {
        title: "Edition du classement",
        addTitle: "Ajouter un champ personnalisé",
        optPh: "Sélectionnez un type de champ",
        labelType: "Type de champ à ajouter au classement :",
        opt1: "Texte (Ex : une description, un commentaire, une adresse, ...)",
        opt2: "Date (Ex: une date de visite, une date de visionnage, ...)",
        opt3: "Score (Ex : une note du scénario sur 20, un score du goût d'un plat sur 5, ...)",
        nameLabel: "Nom du champ :",
        namePh: "Adresse du restaurant, Note du scénario /20, Date de réalisation de ce plat, ...",
        scoreMaxLabel: "Score maximum (saisissez 20 si la note sera attribuée sur 20) :",
        addButton: "Ajouter le champ",
        tabFieldName: "Nom du champ",
        tabFieldType: "Type de champ",
        tabFieldActions: "Actions",
        tabFieldAutres: "Autres propriétés de champ",
        scoreMax: "Score maximum",
        successDeletedTitle: "Confirmation de suppression",
        successDeletedContent: "Le champ a été supprimé avec succès de la liste.",
        failDeletedTitle: "Erreur",
        failDeletedContent: "Une erreur s'est produite lors de la suppression du champ.",
        error: "Impossible de récupérer la liste avec cet identifiant ou bien vous n'avez pas les droits (ce n'est pas votre liste).",
        empty: "Ce classement n'a pas encore de champ personnalisé :-( ... Vous pouvez en ajouter un avec le formulaire ci-dessous.",
        errorAdd: "Erreur lors de l'ajout du champ. Veuillez réessayer.",
        missingFieldName: "^Le champ doit posséder un nom.",
        sizeFieldName: "^Le nom du champ doit être compris entre 1 et 40 caractères.",
        formatScoreMax: "^Le score maximum doit être un nombre positif supérieur ou égal à 1.",
        missingType: "^Le type de champ doit être défini."

    },
    loader: {
        text: "Chargement des données en cours..."
    },
    toast: {
        now: "Maintenant"
    },
    field: {
        text : "Texte",
        date: "Date",
        score: "Score / Note"
    },
    listview: {
        tabEntryName: "Nom de l'entrée",
        tabFieldDateAdded: "Date d'ajout",
        tabFieldScoreTotal: "Score cumulé",
        tabActions: "Actions",
        addEntry: "Ajouter une nouvelle entrée",
        switchToRecent: "Passer en affichage le plus récent d'abord",
        switchToClassement: "Passer en affichage classement à points",
        backward: "Revenir à la liste des classements",
        error: "Impossible de récupérer la liste avec cet identifiant ou bien vous n'avez pas les droits (ce n'est pas votre liste).",
        empty: "Ce classement n'a pas encore d'entrées :-( ... Vous pouvez en ajouter une avec le bouton ci-dessus.",
        addEntryTitleModal: "Ajouter une nouvelle entrée",
        modifyEntryButtonModal: "Modifier l'entrée",
        addEntryButtonModal: "Ajouter l'entrée",
        entryNameLabel: "Nom de l'entrée :",
        entryNamePh: "La Casa De Papel, Pizzeria Basilic & Co, Hotel Georges V, ...",
        confirmSuppress: "Oui, supprimer l'entrée.",
        cancelSuppress: "Retour",
        titleSuppress: "Confirmation de suppression",
        textSuppress: "Voulez-vous vraiment supprimer l'entrée suivante : ",
        titleToast: "Information",
        textSuppressSuccess : "L'entrée a bien été supprimée",
        textSuppressFail: "Erreur lors de la suppression de l'entrée",
        descr: "Vous pouvez ajouter une nouvelle entrée avec le bouton ci-dessous. Si vous souhaitez éditer une entrée existante, double-cliquez sur cette dernière.",
        descr2: "Basculez du mode 'affichage récent' à 'affichage par points' si votre classement comporte des champs de type 'note' avec le bouton situé à droite.",
        errorServer: "Une erreur s'est produite côté serveur, veuillez réessayer."
    }
    
};
