package org.scribe.examples;

import java.io.PrintWriter;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class GoogleExample
{
  private static final String NETWORK_NAME = "Google";
  private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
  private static final String PROTECTED_RESOURCE_URL = "https://mail.google.com/mail/feed/atom";
  private static final String SCOPE = "https://mail.google.com/mail"; 

  
  public static String getOAuthCode(){
	  
	  OAuthService service = new ServiceBuilder()
      .provider(GoogleApi.class)
      .apiKey("anonymous")
      .apiSecret("anonymous")
      .scope(SCOPE)
      .build();
	  Scanner in =new Scanner(System.in);
	  System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
	  System.out.println();
	  
	  //Obtain Request token
	  System.out.println("Fetching request token");
	  Token requestToken = service.getRequestToken();
	  System.out.println("The request token"+requestToken);
	  System.out.println("Authorizing Scribe");
	  System.out.println();
	  System.out.println(AUTHORIZE_URL + requestToken.getToken());
	  System.out.println("And paste the verifier here");
	  System.out.print(">>");
	  Verifier verifier = new Verifier(in.nextLine());
	  System.out.println();
	
	  System.out.println("Trading the Request Token for an Access Token...");
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    System.out.println("Got the Access Token!");
	    System.out.println("(if your curious it looks like this: " + accessToken + " )");
	    System.out.println();

	  return accessToken.getToken();
	  
	  
	  
  }
  
  public static void main(String[] args)
  {
    OAuthService service = new ServiceBuilder()
                                  .provider(GoogleApi.class)
                                  .apiKey("anonymous")
                                  .apiSecret("anonymous")
                                  .scope(SCOPE)
                                  .build();
    Scanner in = new Scanner(System.in);
   try{
    PrintWriter writer =new PrintWriter("out.txt","UTF-8");
    writer.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
    writer.println();

    // Obtain the Request Token
    writer.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    writer.println("Got the Request Token!");
    writer.println("(if your curious it looks like this: " + requestToken + " )");
    writer.println();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(AUTHORIZE_URL + requestToken.getToken());
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    writer.println();

    // Trade the Request Token and Verfier for the Access Token
    writer.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);
    writer.println("Got the Access Token!");
    writer.println("(if your curious it looks like this: " + accessToken + " )");
    writer.println();

    // Now let's go and ask for a protected resource!
   
    writer.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
    service.signRequest(accessToken, request);
    request.addHeader("GData-Version", "3.0");
    Response response = request.send();
    writer.println("Got it! Lets see what we found...");
   // System.exit(1);
    writer.println();
    writer.println(response.getCode());
    writer.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
   }
   catch(Exception e){
	   e.printStackTrace();
   }
  }
}