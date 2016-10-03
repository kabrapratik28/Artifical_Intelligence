#first elemet is start and last element as end
infinite = 9999 #infinite value defined
def min_heuristic():
	global open_list
	max_value = infinite #infinite
	key_return = '' 
	for key , value  in open_list.iteritems():
		if value < max_value : 
			max_value = value 
			key_return = key 
	return key_return 

def cal_huristic_of_next(element):
	global air_distance
	global open_list
	global close_list
	global distance_uptil_now_of_this_element
	global graph_array
	global no_of_dim
	from_this_distances = graph_array[element]
	for ii in range(no_of_dim):
		if ii not in close_list and ii != element and from_this_distances[ii]!=infinite: # dont consider if in close list, same node, infinite edge
			heuristic_calculated = distance_uptil_now_of_this_element[element] + from_this_distances[ii] + air_distance[ii] ## currently explored element distance + distance from current to next + air distance ==> this is heuristica calculated
			if open_list.has_key(ii):   ## check is it has key present
				if open_list[ii] > heuristic_calculated : ## check if heuristic isgreter then only update
					open_list[ii] = heuristic_calculated
					distance_uptil_now_of_this_element[ii] = distance_uptil_now_of_this_element[element] + from_this_distances[ii]
			else :  ##if key not present add to open list 
				open_list[ii] = heuristic_calculated
	       			distance_uptil_now_of_this_element[ii] = distance_uptil_now_of_this_element[element] + from_this_distances[ii]
			
	del(open_list[element]) # delete from open_list
	close_list.append(element) # add to close list
				
no_of_dim = int(raw_input("Give No of dimension"))
graph_array = []
air_distance = [] 

open_list = {}  #open list and heuristic with it
close_list = []  #close list and hheuristic with it
distance_uptil_now_of_this_element = [] 

for k in range(no_of_dim):
	graph_array.append([])
	distance_uptil_now_of_this_element.append(infinite)

distance_uptil_now_of_this_element[0] = 0 # first element is zero

for k in range(no_of_dim):
	for l in range(no_of_dim):
		graph_array[k].append(int(raw_input("Value of x="+str(k)+ ": "+"y= "+str(l)+" : ")))

for k in range(no_of_dim):
	air_distance.append(int(raw_input("Value for x= "+str(k)+" :")))

print ''
open_list[0] = air_distance[0] # first element starting at open list

element_current_min = min_heuristic()

while(element_current_min != (no_of_dim-1) and len(open_list) ):  ## while final goal not achieved
	cal_huristic_of_next(element_current_min)
	element_current_min = min_heuristic()
	print open_list
	print close_list
	print distance_uptil_now_of_this_element
	print element_current_min
	print '---------'

print "Answer"
print open_list
print close_list
print distance_uptil_now_of_this_element

print "Final min distance"
print distance_uptil_now_of_this_element[(no_of_dim-1)]
