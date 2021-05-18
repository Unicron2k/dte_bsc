import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
Skriv tester:
        Testene skal dekke alle scenarier (alle mulige måter en kan tenke seg å bruke en stack på).
        Siden dette er en generisk klasse, test den for minst to ulike typer (Hva om noen bruker pop eller peek på en tomt stack?).
        Husk å få med en test der stakken må dobles for å få plass til en ny push().
        Introduser gjerne en ny public metode capacity() som svarer på stackens nåværende kapasitet.
        Lag også (hvis du ikke har gjort) en constructor til stacken som tar inn start kapasitet (fra før har du en som lager en standard kapasitet).
**/


public class GenericStackTest {
    GenericStack<Integer> gsInteger;

    @BeforeEach
    public void GenericArray_prepare_Integers(){
        gsInteger = new GenericStack<>(4);
        gsInteger.push(1);
        gsInteger.push(2);
        gsInteger.push(3);
    }

    @Test
    public void GenericArray_pushExceedsCapacity_ShouldSucceed(){
        gsInteger.push(4);
        gsInteger.push(5);
        gsInteger.push(6);
        assertThat(gsInteger.capacity(), is(equalTo(8)));
    }

    @Test
    public void GenericArray_toStringPrintsCorrectlyWithPushPeekPop_ShouldSucceed(){
        gsInteger.push(4);
        gsInteger.push(5);
        gsInteger.push(6);
        gsInteger.peek();
        gsInteger.pop();
        assertThat(gsInteger.toString(), is(equalTo("Stack: [1, 2, 3, 4, 5]")));

    }

    @Test
    public void GenericArray_peekNormal_ShouldSucceed(){
        assertThat(gsInteger.peek().toString(), is(equalTo("3")));
    }
    @Test
    public void GenericArray_peekEmpty_ShouldSucceed(){
        gsInteger.pop();
        gsInteger.pop();
        gsInteger.pop();
        assertThat(gsInteger.peek(), is(nullValue()));
    }

    @Test
    public void GenericArray_popNormal_ShouldSucceed(){
        assertThat(gsInteger.pop().toString(), is(equalTo("3")));
    }
    @Test
    public void GenericArray_popEmpty_ShouldSucceed(){
        gsInteger.pop();
        gsInteger.pop();
        gsInteger.pop();
        assertThat(gsInteger.pop(), is(nullValue()));
    }
}