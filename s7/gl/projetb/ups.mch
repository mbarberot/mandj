MACHINE
    ups
    
SETS
    COLIS = { 		/* Les colis du système */
        null,
        col1,
        col2,
        col3,
        col4,
        col5
    };
    ADMIN = { 		/* Les administrateurs du système */
        ch1,ch2,
        dis10,dis11,dis12,dis13,dis14,dis15,dis16,dis17,dis18,dis19,
        dis20,dis21,dis22,dis23,dis24,dis25,dis26,dis27,dis28,dis29,
        dis30,dis31,dis32,dis33,dis34,dis35,dis36,dis37,dis38,dis39,
        dis40,dis41,dis42,dis43,dis44,dis45,dis46,dis47,dis48,dis49,
        dis50,dis51,dis52,dis53,dis54,dis55,dis56,dis57,dis58,dis59,
        dis60,dis61,dis62,dis63,dis64,dis65,dis66,dis67,dis68,dis69,
        dis70,dis71,dis72,dis73,dis74,dis75,dis76,dis77,dis78,dis79,
        tri1,tri2,tri3,tri4,tri5,tri6,tri7
    };			
    CLIENT ;		/* Les clients du système */
    DEST			/* Les destinataires du système (cf fonction retirer_colis) */
    
VARIABLES
    centre,
    centre_tri,
	centre_tri2Region,
    centre_distri,
	centre_distri2Zone,	
    distri2tri,
	centre2ColisAttendus,	
    camion,
    colis,
    colisPerdus,
    colis2centre,
    colis2camion,
    colis2client,
    colis2RegDestin,
	colis2ZoneDestin,
    entree,
    historique
	

INVARIANT      
    /* L'INFRASTRUCTURE */
    /* Les centres ont un administrateur */
    /* Les centres de tri font partie des centres */
	/* Les centres de tri sont associés à une région */
    /* Les centres de distribution font partie des centres */
	/* Les centres de distribution sont associés à une zone */
    /* Les camions ont un administrateur : le chauffeur */
    /* Les centres ne peuvent pas être à la fois de distribution et de tri */
    /* Un administrateur ne peut gérer à la fois un centre et être un chauffeur de camion */
    centre <: ADMIN &
    centre_tri <: centre & 
	centre_tri2Region :  centre_tri >-> NAT &
	centre_distri <: centre &
	centre_distri2Zone : centre_distri >-> NAT&
    distri2tri : centre_distri -->> centre_tri &
    camion <: ADMIN &
    centre_tri /\ centre_distri = {} &
    centre /\ camion = {} &
    
    /* LES COLIS */
    /* Les colis sont l'ensemble des colis du système */	
    /* On peut connaitre dans quel centre se trouve le colis avec "colis2centre" */
    /* On peut connaitre dans quel camion se trouve le colis avec "colis2camion" */
    /* On peut connaitre le client qui à déposé le colis avec "colis2client" */
    /* On peut connaitre le premier chiffre du code postal du destinataire du colis avec "colis2RegDestin" */
	/* La région de destination du colis doit exister (=> un centre de tri doit exister dans cette région) */
	/* On peut connaitre le second chiffre du code postal du destinataire du colis avec "colis2ZoneDestin" */
	/* La zone de destination du colis doit exister (=> un centre de distribution doit exister dans cette zone) */
	/* Le colis ne peut être dans un camion et dans un centre en même temps */
	/* Le colis peut être dans un centre à la fois */
	/* Le colis ne peut etre que dans un camion à la fois */
	
	colis <: COLIS &
	colisPerdus <: colis &
    colis2centre : colis -->> centre &
    colis2camion : colis -->> camion &
    colis2client : colis -->> CLIENT &
    colis2RegDestin : colis -->> NAT &
	!(col).(col : colis => (colis2RegDestin(col) >= 0 & colis2RegDestin(col) < card(ran(centre_tri2Region)))) & 
    colis2ZoneDestin : colis -->> NAT &
    !(col).(col : colis => (colis2ZoneDestin(col) >= 0 & colis2ZoneDestin(col) < card(ran(centre_distri2Zone)))) &

	!(col).(col : colis => not( col : dom(colis2centre) & col : dom(colis2camion) )) &
	!(col).(col : colis => card(colis2centre[{col}]) <= 1 & card(colis2camion[{col}]) <= 1) &

	/* VERIFICATIONS */
	/* A chaque transfert, on mémorise les colis attendus pour le centre d'arrivée */
	/* On pourra vérifier ainsi les pertes (avec la variable colis2centre) */
	/* La variable nous permettra de plus de trier les colis dans les centres dédiés à cet effet */
	/* Restriction : un colis ne peut être attendu que dans un seul centre à un moment donné*/
	centre2ColisAttendus : centre >+> colis &
	
    /* LE TRACAGE */
    /* Une entrée permet de lier un colis à l'administrateur de l'étape */
    /* L'historique est un ensemble de deux entrées : l'entrée précédente et l'entrée actuelle */
    /* La première entrée "dépôt" sera le couple { dépôt |-> dépôt } dans l'historique */
    /* Le dernière entrée est accessible via l'historique grâce au fait qu'elle fait partie du seul couple { a |-> b } tel que b n'apparait dans aucun couple { b |-> c } */
    entree : colis -->> ADMIN &
    historique : entree >->> entree 

INITIALISATION
    centre := { 
        dis10,dis11,dis12,dis13,dis14,dis15,dis16,dis17,dis18,dis19,
        dis20,dis21,dis22,dis23,dis24,dis25,dis26,dis27,dis28,dis29,
        dis30,dis31,dis32,dis33,dis34,dis35,dis36,dis37,dis38,dis39,
        dis40,dis41,dis42,dis43,dis44,dis45,dis46,dis47,dis48,dis49,
        dis50,dis51,dis52,dis53,dis54,dis55,dis56,dis57,dis58,dis59,
        dis60,dis61,dis62,dis63,dis64,dis65,dis66,dis67,dis68,dis69,
        dis70,dis71,dis72,dis73,dis74,dis75,dis76,dis77,dis78,dis79,
        tri1,tri2,tri3,tri4,tri5,tri6,tri7
    } ||
    centre_tri := { tri1,tri2,tri3,tri4,tri5,tri6,tri7 } ||
	centre_tri2Region := { tri1 |-> 1,tri2 |-> 2,tri3 |-> 3, tri4 |-> 4, tri5 |-> 5, tri6 |-> 6, tri7 |-> 7 } ||
    centre_distri := {
    	dis10,dis11,dis12,dis13,dis14,dis15,dis16,dis17,dis18,dis19,
        dis20,dis21,dis22,dis23,dis24,dis25,dis26,dis27,dis28,dis29,
        dis30,dis31,dis32,dis33,dis34,dis35,dis36,dis37,dis38,dis39,
        dis40,dis41,dis42,dis43,dis44,dis45,dis46,dis47,dis48,dis49,
        dis50,dis51,dis52,dis53,dis54,dis55,dis56,dis57,dis58,dis59,
        dis60,dis61,dis62,dis63,dis64,dis65,dis66,dis67,dis68,dis69,
        dis70,dis71,dis72,dis73,dis74,dis75,dis76,dis77,dis78,dis79
    } ||
	centre_distri2Zone := {} ||
    distri2tri := {
        dis10 |-> tri1, dis11 |-> tri1, dis12 |-> tri1, dis13 |-> tri1, dis14 |-> tri1,
        dis15 |-> tri1, dis16 |-> tri1, dis17 |-> tri1, dis18 |-> tri1, dis19 |-> tri1,
        dis20 |-> tri2, dis21 |-> tri2, dis22 |-> tri2, dis23 |-> tri2, dis24 |-> tri2,
        dis25 |-> tri2, dis26 |-> tri2, dis27 |-> tri2, dis28 |-> tri2, dis29 |-> tri2,
        dis30 |-> tri3, dis31 |-> tri3, dis32 |-> tri3, dis33 |-> tri3, dis34 |-> tri3,
        dis35 |-> tri3, dis36 |-> tri3, dis37 |-> tri3, dis38 |-> tri3, dis39 |-> tri3,
        dis40 |-> tri4, dis41 |-> tri4, dis42 |-> tri4, dis43 |-> tri4, dis44 |-> tri4,
        dis45 |-> tri4, dis46 |-> tri4, dis47 |-> tri4, dis48 |-> tri4, dis49 |-> tri4,
        dis50 |-> tri5, dis51 |-> tri5, dis52 |-> tri5, dis53 |-> tri5, dis54 |-> tri5,
        dis55 |-> tri5, dis56 |-> tri5, dis57 |-> tri5, dis58 |-> tri5, dis59 |-> tri5,
        dis60 |-> tri6, dis61 |-> tri6, dis62 |-> tri6, dis63 |-> tri6, dis64 |-> tri6,
        dis65 |-> tri6, dis66 |-> tri6, dis67 |-> tri6, dis68 |-> tri6, dis69 |-> tri6,
        dis70 |-> tri7, dis71 |-> tri7, dis72 |-> tri7, dis73 |-> tri7, dis74 |-> tri7,
        dis75 |-> tri7, dis76 |-> tri7, dis77 |-> tri7, dis78 |-> tri7, dis79 |-> tri7 
    } ||
    camion := { 
        ch1,
        ch2
    } ||
    colis := {} ||
	colisPerdus := {} ||
    colis2centre := {} ||
    colis2camion := {} ||
    colis2client := {} ||
    colis2RegDestin := {} ||
    colis2ZoneDestin := {} ||     
    
    centre2ColisAttendus := {} ||
    
 	entree := {} ||
    historique := {}
    
    
OPERATIONS
    /* Ajout d'un nouveau colis dans le système */
    nouveau_colis(nouv_colis,reg,zone) =
    PRE
        nouv_colis : COLIS &
        nouv_colis /: colis &
        reg : NAT &
		zone : NAT
    THEN
        colis := colis \/ { nouv_colis } ||
        colis2RegDestin := colis2RegDestin \/ { nouv_colis |-> reg } ||
		colis2ZoneDestin := colis2ZoneDestin \/ { nouv_colis |-> zone }
    END;
    
    
    /* Dépôt d'un colis : à domicile */
    avis <-- depot_colis_camion(client,lecolis,lecamion,lecentre) =
    PRE
        client : CLIENT &
        lecolis : COLIS & lecolis : colis &
        lecamion : ADMIN & lecamion : camion &
		lecentre : ADMIN & lecentre : centre_distri
    THEN
        colis2client := colis2client \/ { lecolis |-> client } ||
        colis2camion := colis2camion \/ { lecolis |-> lecamion } ||
		centre2ColisAttendus := centre2ColisAttendus \/ {lecentre |-> lecolis} ||
		avis := lecolis ||
		historique := historique \/ { (lecolis |-> lecamion) |-> (lecolis |-> lecentre)} 
	
    END;
    
    /* Dépôt d'un colis : au centre de distribution */
    avis <-- depot_colis_centre(client,lecolis,lecentre) =
    PRE
        client : CLIENT &
        lecolis : COLIS & lecolis : colis &
        lecentre : ADMIN & lecentre : centre_distri
    THEN
        colis2client := colis2client \/ { lecolis |-> client } ||
        colis2centre := colis2centre \/ { lecolis |-> lecentre } ||
        avis := lecolis ||
        historique := historique \/ { (lecolis |-> lecentre) |-> (lecolis |-> lecentre)} 
    END;
    
    
    /* Envoi de colis inter-centres : départ */
    /* On connaît le centre de départ avec la variable colis2centre. Pour le centre d'arrivée, deux cas de figure :*/
    /* Le colis a été trié : le centre d'arrivée est associé au colis dans centre2ColisAttendus */
    /* Le colis n'a pas été trié : si on se trouve dans un centre de distribution, on envoie alors le colis dans le centre de tri le plus proche */
    envoi_colis(le_colis, le_camion) =
    PRE
        le_colis : COLIS & le_colis : colis &        
        le_camion : ADMIN & le_camion : camion &		
		le_colis : dom(colis2centre)
    THEN
        IF(le_colis /: ran(centre2ColisAttendus) & colis2centre(le_colis) : centre_distri) 
        THEN 
        	centre2ColisAttendus := centre2ColisAttendus \/ {distri2tri(colis2centre(le_colis)) |-> le_colis }       
        END||        
        colis2camion := colis2camion \/ {le_colis |-> le_camion} ||
        colis2centre := {le_colis} <<| colis2centre ||
        historique := historique \/ { (le_colis |-> colis2centre(le_colis)) |-> (le_colis |-> le_camion) }		
    END;    

	/* Traite la bonne réception d'un colis */
    reception_colis(le_colis, c_arrivee, le_camion) =
    PRE
 		le_colis : COLIS & le_colis : colis &
 		c_arrivee : ADMIN & c_arrivee : centre &
 		le_camion : ADMIN & le_camion : camion & 		
 		le_colis : centre2ColisAttendus[{c_arrivee}] &
 		le_colis : colis2camion~[{le_camion}] 		
	THEN		        
	    colis2camion := {le_colis} <<| colis2camion ||
	    colis2centre := colis2centre \/ {le_colis |-> c_arrivee} ||
	    centre2ColisAttendus := centre2ColisAttendus |>> {le_colis} ||
	    historique := historique \/ { (le_colis |-> le_camion) |-> (le_colis |-> c_arrivee) }        		
    END;
    
    /* Permet à un administrateur de signaler la perte d'un colis */
	signaler_perte_colis(le_colis, l_admin) =
	PRE
	    le_colis : COLIS & le_colis : colis &
	    l_admin : ADMIN 
	THEN
	    colisPerdus := colisPerdus \/ {le_colis}
	END;
	
	/* Permet de retrouver le dernier responsable d'un colis perdu */
	resp <-- retrouver_colis(le_colis) =
	PRE
	    le_colis : COLIS & le_colis : colis &
	    le_colis : colisPerdus &
	    resp : ADMIN
	THEN
	    /* Le colis a été perdu dans un camion */
	    IF(le_colis : dom(colis2camion) )
	    THEN
			resp := colis2camion(le_colis)
		/* Le colis a été perdu dans un centre */
	    ELSIF (le_colis : dom(colis2centre))
	    THEN        
	            resp := colis2centre(le_colis)
	    END	    
	END;	
    

    /* Tri des colis selon le premier chiffre du code postal. Si le colis est déjà dans le centre de tri de sa région de destination, on utilisera la fonction pour le tri par zone */
    tri_colis_region(le_colis, c_tri) =
    PRE
        le_colis : COLIS & le_colis : colis &
        le_colis : dom(colis2RegDestin) &
		c_tri : ADMIN &		
        c_tri : centre_tri &
		colis2centre(le_colis) = c_tri
    THEN
        IF(centre_tri2Region(c_tri) /= colis2RegDestin(le_colis))
        THEN /* On "redirige" le colis vers le centre de tri de sa région de destination */
			centre2ColisAttendus:= centre2ColisAttendus \/ {centre_tri2Region~(colis2RegDestin(le_colis)) |-> le_colis}           
		END ||

        historique :=  historique \/ { (le_colis |-> c_tri) |-> (le_colis |-> c_tri) }
    END;   

    /* Tri des colis par second chiffre du code postal */
    tri_colis_zone(le_colis, c_tri) =
    PRE
		le_colis : COLIS & le_colis : colis &
        le_colis : dom(colis2ZoneDestin) &
		c_tri : ADMIN &		
        c_tri : centre_tri &
		colis2centre(le_colis) = c_tri
    THEN
		/* On envoie le colis vers le centre de distribution de sa zone de destination */
		ANY c_distri WHERE c_distri : centre_distri & distri2tri(c_distri) = c_tri & centre_distri2Zone(c_distri) = colis2ZoneDestin(le_colis)
		THEN
			centre2ColisAttendus := centre2ColisAttendus \/ {c_distri |-> le_colis }
		END ||
		historique :=  historique \/ { (le_colis |-> c_tri) |-> (le_colis |-> c_tri) }
    END;

    
    /* Livraison du colis au destinataire */
    avis <-- livrer_colis(lecolis,chezlui) =
    PRE
        lecolis : COLIS & 
        lecolis : colis &            
        chezlui : BOOL
    THEN
	
        IF chezlui = TRUE
        THEN
            colis2centre := {lecolis} <<| colis2centre ||
			colis2RegDestin := {lecolis} <<| colis2RegDestin ||
            colis2ZoneDestin := {lecolis} <<| colis2ZoneDestin ||
            colis := colis - {lecolis}
            
        ELSE
            avis := lecolis
        END    
    END;
    
    /* Retirer le colis au centre de distribution */
    retirer_colis(avis,reg,zone) = 
    PRE
        avis : COLIS &
        avis : colis &
        reg : NAT &
		zone : NAT
    THEN	
        IF (( avis |-> reg ) : colis2RegDestin & (avis |-> zone ) : colis2ZoneDestin ) 
        THEN
			colis2centre := {avis} <<| colis2centre ||
            colis2RegDestin := {avis} <<| colis2RegDestin ||
            colis2ZoneDestin := {avis} <<| colis2ZoneDestin ||
            colis := colis - {avis}
        END
    END


END
