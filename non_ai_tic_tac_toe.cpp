#include <iostream>
using namespace std;

   int tictacteo[3][3] ={{2,2,2},{2,2,2},{2,2,2}} ;

   int wining_posibility_1 = 0 ; 
   int wining_posibility_2 = 0 ;

   const int wining_x_became= 50 ; 
   const int wining_y_became= 18 ;
 
   int x,y ; 


void computer_chance()
{
int flag_marked = 0 ; 
int mul_of_opponent = 1 ; 
     for(int io=0 ; io<3; io++)
	{
		mul_of_opponent = 1 ; 
		for(int ko=0; ko<3 ; ko++)
		{
			mul_of_opponent = mul_of_opponent * tictacteo[io][ko] ;
			//cout << mul_of_opponent << " " << tictacteo[io][ko]  << "  \n";	
		}	
		//cout <<"Mul of opponent ay" << io << " is " <<  mul_of_opponent <<"\n";
		if (mul_of_opponent==50)
		{
			flag_marked = 1 ; 
			for(int ko=0; ko<3 ; ko++)
			{
				if (tictacteo[io][ko]==2)
				{
					tictacteo[io][ko] = 3  ;
					break ; 				
				}	
			}
			break ;
		}
	}	
if (flag_marked==0)
{
        for(int io=0 ; io<3; io++)
	{
		mul_of_opponent = 1 ; 
		for(int ko=0; ko<3 ; ko++)
		{
			mul_of_opponent = mul_of_opponent * tictacteo[ko][io] ; 	
		}	
		if (mul_of_opponent==50)
		{
			flag_marked = 1 ; 
			for(int ko=0; ko<3 ; ko++)
			{
				if (tictacteo[ko][io]==2)
				{
					tictacteo[ko][io] = 3  ;
					break ; 				
				}	
			}
			break ;
		}
	}
}


if (flag_marked==0)
{
        mul_of_opponent = 1 ; 
        for(int io=0 ; io<3; io++)
	{
		
			mul_of_opponent = mul_of_opponent * tictacteo[io][io] ; 	
         }	
	       if (mul_of_opponent==50)
		{
			flag_marked = 1 ; 
			for(int ko=0; ko<3 ; ko++)
			{
				if (tictacteo[ko][ko]==2)
				{
					tictacteo[ko][ko] = 3  ;
					break ; 				
				}	
			}
		
		}
	
}

if (flag_marked==0)
{

		
	mul_of_opponent =  tictacteo[0][2]* tictacteo[1][1]  * tictacteo[2][0] ; 		
	       if (mul_of_opponent==50)
		{
			flag_marked = 1 ; 
			if (tictacteo[0][2]==2)
			{
				tictacteo[0][2]=5 ; 
			}
			else if(tictacteo[1][1] ==2)
			{
				tictacteo[1][1]=5 ; 
			}
			else if(tictacteo[2][0] ==2)
			{
				tictacteo[2][0]=5 ; 
			}
		
		}
	
}


if (flag_marked==0)
{
	for (int pp=0 ; pp<3 ;pp++)
	{
		for (int gg=0; gg<3 ; gg++)
		{
			if (tictacteo[pp][gg]==2)
			{
				tictacteo[pp][gg] = 3 ;
				flag_marked = 1 ; 
				break ;
			}			
		}
		if (flag_marked==1)
			{
				break ;		
			}	
	}	

}


}





// main() is where program execution begins.
//- => 2
//o- 3
//x => 5
int main()
{
 

   for(int i=0 ; i< 9 ;i++)
	{
		if (i%2==0)
		{
			cout << "Give x position" ; 				cin >> x ;
			cout << "Give y position" ; 				cin >> y ; 
			tictacteo[x][y]=5 ; 
		}
		else{
			computer_chance() ; 
		}	
		for (int yo=0 ; yo<3 ;yo++)
		{
			for (int go=0;go<3;go++)
			{
				cout << tictacteo[yo][go]<<" " ; 				
			}
			cout <<"\n" ;
		}
		cout << "\n\n\n";
		
	}   
	
      

   return 0;
}
