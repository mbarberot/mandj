/**
 *  loadgraphfromfile
 *  Author: Samuel Thiriot
 *  Description: Shows how to load a graph from a file and how to display it. Nothing "moves" into this first model.
 */

model ctraffic1

global {
	graph citygraph; 
	graph roadgraph;
	init {
		
		 set citygraph <- load_graph_from_dot( [
                        "filename"::"../includes/city.dot",
                        "edges_specy"::road,
                        "vertices_specy"::service] );                        
                                 
		create people number : 10 {
			set location <- any_location_in (one_of(service));
		}
		
		//set roadgraph <- as_edge_graph(list(road));
		
	 }
	  
}

environment ;

entities {
	species service  {
		rgb color <- rgb('red') ;  
		aspect base { 
			draw shape: circle size:3 color: color ;
		} 		 		
	}
	
	species road  { 
		/* Choisit aléatoirement si la rue est à sens unique (ou pas)*/
		bool directed <- flip(0.33);
		rgb color <- rgb('black');
		service dep <- nil;
		service arr <- nil;
		/* Sens unique −> colorée en vert */
		if condition : directed = true
		{
			set dep <- citygraph source_of(self);
			set arr <- citygraph target_of(self);
			write("Depart : " + dep + " / Arrivee : " + arr);
			set color <- rgb('green');
		}		
		 		
		aspect base {
			draw color: color ;			
		}		
	}
	
	species people skills: [moving]{
		rgb color <- rgb('yellow') ;
		point target <- nil; 
		
		reflex set_new_target when: target = nil {
			set target <- any_location_in (one_of(list(service)));
		}
		
		reflex move when: target != nil {
			do goto target: target on: as_edge_graph(list(road)); 
			switch target { 
				match location {set target <- nil ;}
			}
		}
		aspect base { 
			draw shape: circle size:1 color: color ;
		} 	
	}
}

experiment load_city type: gui {
	output {
		display test_display refresh_every: 1 {
			species service aspect: base ; 
			species road aspect: base ;
			species people aspect: base;
		}
		graphdisplay monNom2 graph: citygraph lowquality:true;
	}
}