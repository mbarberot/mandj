/**
 *  ctraffic5
 *  Author: 
 *  Description: model using the "createStreet" statement 
 */

model ctraffic5

global
{
	var roads_file type:string init:"../includes/gamamodel/road.shp";
	var bounds_file type:string init:"../includes/gamamodel/bounds.shp";
	
	graph city_graph;
	init
	{
		create roads from: roads_file with: [waytype::read("TYPE")];
		set city_graph <- directed(as_edge_graph(list(roads)));
			
		create people number : 100 {
			let my_road type: roads <- one_of (list(roads));
			set location <- any_location_in (my_road.shape);
		}
		
		create services number : 50 {
			let my_road type: roads <- one_of (list(roads));
			set location <- any_location_in(my_road.shape);
		}
	}
}

environment bounds:bounds_file;

entities
{
	species services{
		rgb color <- rgb('green');
		aspect basic{
			draw color:color size : 50;
		}
	}
	
	species roads parent:Street
	{
		int idStart;
		int idEnd;
		rgb color <- rgb('black');

		if( waytype = "DOUBLE"){
			if(!clone)
			{
				set color <- rgb('green');
			} else {
				set color <- rgb('red');
			}
		}
		aspect base {
				draw color: color;
		}
	}
	
	species people skills: [moving]{
		rgb color <- rgb('yellow') ;
		services target <- nil; 
		
		reflex search_a_target when: target = nil {
			set target <- one_of(list(services));
		}
		
		action new_target {
			set target <- one_of(list(services));
		}
		reflex move when: target != nil {
			do goto target: target on: city_graph;
			switch target.location { 
				match location {
					write("Objectif atteint");
					set target <- nil;
					do new_target ;			
				}
			}
		}		
		
		aspect base { 
			draw shape: circle size:10 color: color ;
		} 	
	}
	
}

experiment load_city type: gui {
	output {
		display test_display refresh_every: 1 {
			species services aspect: base ; 
			species roads aspect: base ;
			species people aspect: base;
		}
	}
}


