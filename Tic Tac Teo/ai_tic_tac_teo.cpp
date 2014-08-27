#include <iostream>
#include <vector>

using namespace std;
// for indexing max element
template <class ForwardIterator>
  std::size_t max_element_index ( ForwardIterator first, ForwardIterator last )
{
  ForwardIterator highest = first;
  std::size_t index = 0;
  std::size_t i = 0;
  if (first==last) return index;
  while (++first!=last) {
    ++i;
    if (*first>*highest) {
      highest=first;
      index = i;
    }
  }
  return index;
}

int tictacteo[3][3] = { { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 } };

int wining_posibility_1 = 0;
int wining_posibility_2 = 0;

const int wining_x_became = 50;
const int wining_y_became = 18;

int x, y;

int min_max_result() {
	/*
	 *
	 for (int yo = 0; yo < 3; yo++) {
				for (int go = 0; go < 3; go++) {
					cout << tictacteo[yo][go] << " ";
				}
				cout << "\n";
			}
	*/
	int value_of_min_max = 0;

	int user_score = 0, flag_user = 0;
	int comp_score = 0, flag_comp = 0;

	for (int io = 0; io < 3; io++) {
		flag_user = 0;
		flag_comp = 0;
		for (int ko = 0; ko < 3; ko++) {
			if (tictacteo[io][ko] == 5 || tictacteo[io][ko] == 2) {

			} else {
				flag_user = 1;
			}

			if (tictacteo[io][ko] == 3 || tictacteo[io][ko] == 2) {

			} else {
				flag_comp = 1;
			}
		}
		if (flag_comp == 0) {
			//cout << io << " comp added " ;
			comp_score++;
		}
		if (flag_user == 0) {
			//cout << io << " user added " ;
			user_score++;
		}
	}

	for (int io = 0; io < 3; io++) {
		flag_user = 0;
		flag_comp = 0;
		for (int ko = 0; ko < 3; ko++) {
			if (tictacteo[ko][io] == 5 || tictacteo[ko][io] == 2) {

			} else {
				flag_user = 1;
			}

			if (tictacteo[ko][io] == 3 || tictacteo[ko][io] == 2) {

			} else {
				flag_comp = 1;
			}
		}
		if (flag_comp == 0) {
			//cout << io << " comp ver added " ;
			comp_score++;
		}
		if (flag_user == 0) {
			//cout << io << " user ver added " ;
			user_score++;
		}
	}

	flag_user = 0;
	flag_comp = 0;

	for (int io = 0; io < 3; io++) {
		if (tictacteo[io][io] == 5 || tictacteo[io][io] == 2) {

		} else {
			flag_user = 1;
		}

		if (tictacteo[io][io] == 3 || tictacteo[io][io] == 2) {

		} else {
			flag_comp = 1;
		}
	}
	if (flag_comp == 0) {
		//cout << " comp right cross added " ;
		comp_score++;
	}
	if (flag_user == 0) {
		//cout <<  " user right added " ;
		user_score++;
	}

	flag_user = 0;
	flag_comp = 0;

	if ((tictacteo[0][2] == 3 || tictacteo[0][2] == 2)
			&& (tictacteo[1][1] == 3 || tictacteo[1][1] == 2)
			&& (tictacteo[2][0] == 3 || tictacteo[2][0] == 2)) {
		//cout << " comp left cross added " ;
		comp_score++;
	}
	if ((tictacteo[0][2] == 5 || tictacteo[0][2] == 2)
			&& (tictacteo[1][1] == 5 || tictacteo[1][1] == 2)
			&& (tictacteo[2][0] == 5 || tictacteo[2][0] == 2)) {
		//cout << " user left cross added " ;
		user_score++;
	}

	value_of_min_max = comp_score - user_score;
	return value_of_min_max;
}

void computer_chance() {
	vector<int> x_position ;
	vector<int> y_position ;
	vector<int> val_min_max_algo  ;
	int min_for_this = 100 ;

	for (int tt = 0; tt < 3; tt++) {
		for (int dd=0; dd<3 ;dd++){               // for comp mark
				if (tictacteo[tt][dd]== 3 || tictacteo[tt][dd]== 5 )
				{
					// already marked dont consider
				}
				else{
					x_position.push_back(tt);
					y_position.push_back(dd);
					min_for_this = 100 ; // now see value whts minimum
					for (int qw=0 ; qw<3 ; qw ++)            // 2 ply user mark
					{
						for(int rt=0; rt<3 ;rt++)
						{
							if ((tictacteo[qw][rt]== 3 || tictacteo[qw][rt]== 5) || (qw==tt && rt ==dd)  ) // at same position cant place
								{
									// already marked dont consider
								}
							else
							{

								tictacteo[tt][dd]= 3 ;
								tictacteo[qw][rt]=5 ;

								int temp = min_max_result();

								//cout << tt << "  "<< dd << " " << qw << " "<< rt <<  "    re: "<<temp <<"\n";


								if (temp < min_for_this)
								{
									min_for_this = temp ;
								}
								tictacteo[tt][dd]= 2 ; //make again bz for calculatopn we are putting
								tictacteo[qw][rt]=2 ;

							}
						}
					}
					val_min_max_algo.push_back(min_for_this);
				}
		}
	}

  int maximum_value_index = max_element_index(val_min_max_algo.begin(),val_min_max_algo.end());
  tictacteo[x_position[maximum_value_index]][y_position[maximum_value_index]]  = 3 ;  // move @ minimum
}

// main() is where program execution begins.
//- => 2
//o- 3
//x => 5
int main() {

	for (int i = 0; i < 9; i++) {
		if (i % 2 == 0) {
			cout << "Give x position";
			cin >> x;
			cout << "Give y position";
			cin >> y;
			tictacteo[x][y] = 5;
		} else {
			computer_chance();
			/*
			cout << "Give comp x position";
			cin >> x;
			cout << "Give comp y position";
			cin >> y;
			tictacteo[x][y] = 3; */
		}
		for (int yo = 0; yo < 3; yo++) {
			for (int go = 0; go < 3; go++) {
				cout << tictacteo[yo][go] << " ";
			}
			cout << "\n";
		}
		cout << "Min Max Heuristic : "<<min_max_result();
		cout << "\n\n\n";

	}

	return 0;
}
