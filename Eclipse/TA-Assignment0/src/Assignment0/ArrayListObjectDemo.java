package Assignment0;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListObjectDemo {

	/*public static void main2(String[] args) {
		
		//ArrayList functions
		Animal dog1 = new Animal("spot", 5, "dalmation");
		Animal dog2 = new Animal("caesar", 10, "german shepherd");
		
		ArrayList<Animal> dogs = new ArrayList<Animal>();
		dogs.add(dog1);
		dogs.add(dog2);
		dogs.add(new Animal("odie", 20, "who knows"));
		
		for(Animal a : dogs){
			out.println(a);
		}
		
		out.println(Arrays.toString(dogs.toArray()));
		
		System.gc();
	}*/
	
	public static void main(String [] args){
		Animal a1 = new Animal();   Animal a2 = new Platypus();
		Platypus p1 = new Animal();
	    Mammal m1 = new Platypus();    System.out.print( a1.bt() );    System.out.print( a2.bt() );
	    System.out.print( m1.bt() );

	}

}


class Animal
{  public String bt(){ return "!"; }}
class Mammal extends Animal
{
  public String bt(){ return "live"; }
}
class Platypus extends Mammal
{  public String bt(){ return "egg";}
}

