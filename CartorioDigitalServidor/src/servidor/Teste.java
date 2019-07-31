/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author User
 */
public class Teste {
    public static void main(String[] args){
        String helloWorld = "Hello World!";
 
          helloWorld = Base64.encodeBase64String(helloWorld.getBytes());
           
       System.out.println("String codificada " + helloWorld);
  
       //
       // Decodifica uma string anteriormente codificada usando o método decodeBase64 e
       // passando o byte[] da string codificada
       //
       byte[] decoded = Base64.decodeBase64(helloWorld.getBytes());
  
       //
       // Imprime o array decodificado
       //
       System.out.println(Arrays.toString(decoded));
  
       //
       // Converte o byte[] decodificado de volta para a string original e imprime
       // o resultado.
       //
       String decodedString = new String(decoded);
       System.out.println(helloWorld + " = " + decodedString);
    }
}
