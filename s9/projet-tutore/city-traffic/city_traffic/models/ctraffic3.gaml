/**
 *  ctraffic3
 *  Description: 
 */

model ctraffic3

global
{
	var services_file type: string init:"../includes/services.shp";
	var roads_file type:string init:"../includes/roads.shp";
	var bounds_file type:string init:"../includes/bounds.shp";
	graph city_graph;
	init
	{
		create services from: services_file;
		create streets from: roads_file;
		
		
		/*create 
			species:myStreet 
			from: roads_file 
			returns:streets 
			with:[waytype::read("type"), idstart::read("idStart"), idend::read("idEnd")];*/
			
		set city_graph <- as_edge_graph(list(streets));
		create people number : 10 {
			set location <- any_location_in (one_of(list(services)));
		}
	}
}

environment bounds:bounds_file;

entities
{
	species services
	{
		rgb color <- rgb('red') ;  
		aspect base { 
			draw shape: circle size:15 color: color ;
		} 
	}
	
	species streets
	{
		rgb color <- rgb('black');
		/*
		string oneWay <- waytype;
		
		if condition:oneWay = "DOUBLE"
		{
			set color <- rgb('green');
		}
		*/
		
		aspect base 
		{
			draw shape:line color: color ;			
		}
	}
	
	
	
	species people skills: [moving,citymoving]{
		rgb color <- rgb('yellow') ;
		point target <- nil; 
		string hello <- nil;
		
		reflex set_new_target when: target = nil {
			set target <- any_location_in(one_of(services));
		}
		
		reflex move when: target != nil {
			do goto target: target on: city_graph;
		
			/*do driveto target: target on: city_graph;*/
		
			switch target { 
				match location {
					write("Objectif atteint");
					set target <- nil ;					
				}
			}
		}
		
		
		
		aspect base { 
			draw shape: circle size:8 color: color ;
		} 	
	}
	
}
experiment load_city type: gui {
	output {
		display test_display refresh_every: 1 {
			species services aspect: base ; 
			species streets aspect: base;
			species people aspect: base;
		}
	}
}

