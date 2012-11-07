/**
 *  ctraffic2
 *  Description: 
 */

model ctraffic2

global
{
	var services_file type: string init:"../includes/services.shp";
	var roads_file type:string init:"../includes/roads.shp";
	var bounds_file type:string init:"../includes/bounds.shp";
	init
	{
		create species: services from: services_file;
		create species: roads from: roads_file with: [directed::read("type"), idStart::read("idStart"), idEnd::read("idEnd")];
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
	
	species roads
	{
		string directed;
		int idStart;
		int idEnd;
		rgb color <- rgb('black');
		
		if condition:directed = "DOUBLE"
		{
			set color <- rgb('green');
		}
		
		aspect base {
			draw color: color ;			
		}
	}	
	
	
	experiment load_city type: gui {
	output {
		display test_display refresh_every: 1 {
			species services aspect: base ; 
			species roads aspect: base ;
		}
	}
}
}

