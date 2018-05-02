/*****************************************************
 *
 * NicolaudieAPIAgent.java
 *
 *
 * Copyright (c) 2018, JL
 *
 * All rights reserved.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER(S) AND CONTRIBUTOR(S) "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER(S) OR CONTRIBUTOR(S) BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *****************************************************/

///// Package Declaration /////

package jl.nicolaudietest.api;


///// Import(s) /////



///// Class Declaration /////

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*****************************************************
 *
 * This class is the agent for the Nicolaudie API.
 *
 *****************************************************/
public class NicolaudieAPIAgent
  {
  ////////// Static Constant(s) //////////

  @SuppressWarnings( "unused" )
  static private final String  LOG_TAG                  = "NicolaudieAPIAgent";

  static private final String BASE_URL                  = "http://api.nicolaudiegroup.com:2431/API/v1/";

  static private final String JSON_NAME_USER            = "user";
  static private final String JSON_NAME_CONNECTION_TYPE = "connectionType";
  static private final String JSON_NAME_EMAIL           = "email";
  static private final String JSON_NAME_PASSWORD        = "pwd";
  static private final String JSON_NAME_FIRST_NAME      = "firstname";
  static private final String JSON_NAME_LAST_NAME       = "lastname";
  static private final String JSON_NAME_ACCOUNT_TYPE    = "accountType";

  static private final String JSON_VALUE_CONNECTION_TYPE = "regalade";
  static private final String JSON_VALUE_ACCOUNT_TYPE    = "customer";


  ////////// Static Variable(s) //////////


  ////////// Member Variable(s) //////////

  private Context  mContext;

  private boolean  mCallInProgress;

  private String   mUserToken;


  ////////// Static Initialiser(s) //////////


  ////////// Static Method(s) //////////


  ////////// Constructor(s) //////////

  public NicolaudieAPIAgent( Context context )
    {
    mContext = context;
    }


  ////////// Method(s) //////////

  /*****************************************************
   *
   * Sets the call in progress state.
   *
   *****************************************************/
  private void setCallInProgress( boolean callInProgress )
    {
    mCallInProgress = callInProgress;
    }


  /*****************************************************
   *
   * Returns the call in progress state.
   *
   *****************************************************/
  public boolean isCallInProgress()
    {
    return ( mCallInProgress );
    }


  /*****************************************************
   *
   * Creates an account.
   *
   *****************************************************/
  public void createAccount( String email, String password, Pair<String,String> firstAndSecondNames, ICreateUserCallback callback )
    {
    // Create a JSON object containing the details:
    // {
    //    // shoul only be use to create a standard account (regalade) not for google/facebook account
    //    "user":{
    //    "connectionType":"regalade",
    //            "email":"mail@mail.fr",
    //            "pwd":"userPwd",
    //            "firstname":"Paul",
    //            "lastname":"Dupont",
    //            "accountType":"admin | customer | dev ..."
    //    }
    //    }

    JSONObject rootJSONObject = null;

    try
      {
      JSONObject userJSONObject = new JSONObject();

      userJSONObject.put(JSON_NAME_CONNECTION_TYPE, JSON_VALUE_CONNECTION_TYPE );
      userJSONObject.put(JSON_NAME_EMAIL, email);
      userJSONObject.put(JSON_NAME_PASSWORD, password);
      userJSONObject.put(JSON_NAME_FIRST_NAME, firstAndSecondNames.first);
      userJSONObject.put(JSON_NAME_LAST_NAME, firstAndSecondNames.second);
      userJSONObject.put(JSON_NAME_ACCOUNT_TYPE, JSON_VALUE_ACCOUNT_TYPE );

      rootJSONObject = new JSONObject();
      rootJSONObject.put( JSON_NAME_USER, userJSONObject );

      Log.d( LOG_TAG, rootJSONObject.toString() );
      }
    catch ( JSONException je )
      {
      Log.e( LOG_TAG, "Unable to create JSON", je );

      // TODO: Display an error message to the user

      return;
      }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl( BASE_URL )
            .addConverterFactory( ScalarsConverterFactory.create() )
            .addConverterFactory( GsonConverterFactory.create( gson ) )
            .build();

    INicolaudieAPI nicolaudieAPI = retrofit.create( INicolaudieAPI.class );

    Call<CreateUserResponse> call = nicolaudieAPI.addUser( rootJSONObject.toString() );

    call.enqueue( new AddUserResponseCallback( callback ) );

    setCallInProgress( true );
    }


  /*****************************************************
   *
   * Logs into an account.
   *
   *****************************************************/
  public void logIn( String email, String password, ILogInCallback callback )
    {
    // Create a JSON object containing the details:
    // {
    //    // shoul only be use to create a standard account (regalade) not for google/facebook account
    //    "user":{
    //    "connectionType":"regalade",
    //            "email":"mail@mail.fr",
    //            "pwd":"userPwd"
    //    }
    //    }

    JSONObject rootJSONObject = null;

    try
      {
      JSONObject userJSONObject = new JSONObject();

      userJSONObject.put(JSON_NAME_CONNECTION_TYPE, JSON_VALUE_CONNECTION_TYPE );
      userJSONObject.put(JSON_NAME_EMAIL, email);
      userJSONObject.put(JSON_NAME_PASSWORD, password);

      rootJSONObject = new JSONObject();
      rootJSONObject.put( JSON_NAME_USER, userJSONObject );

      Log.d( LOG_TAG, rootJSONObject.toString() );
      }
    catch ( JSONException je )
      {
      Log.e( LOG_TAG, "Unable to create JSON", je );

      // TODO: Display an error message to the user

      return;
      }

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl( BASE_URL )
            .addConverterFactory( ScalarsConverterFactory.create() )
            .addConverterFactory( GsonConverterFactory.create( gson ) )
            .build();

    INicolaudieAPI nicolaudieAPI = retrofit.create( INicolaudieAPI.class );

    Call<LogInResponse> call = nicolaudieAPI.logIn( rootJSONObject.toString() );

    call.enqueue( new LogInResponseCallback( callback ) );

    setCallInProgress( true );
    }


  /*****************************************************
   *
   * Logs the user out.
   *
   *****************************************************/
  public void logOut()
    {
    mUserToken = null;
    }


  ////////// Inner Class(es) //////////

  /*****************************************************
   *
   * Callback interface for creating an user account.
   *
   *****************************************************/
  public interface ICreateUserCallback
    {
    public void onCreateUserFailure( String message );
    public void onCreateUserSuccess( String userToken );
    }


  /*****************************************************
   *
   * Callback interface for logging in.
   *
   *****************************************************/
  public interface ILogInCallback
    {
    public void onLogInFailure( String message );
    public void onLogInSuccess( String userToken );
    }


  /*****************************************************
   *
   * Listens for an add user response.
   *
   *****************************************************/
  public class AddUserResponseCallback implements Callback<CreateUserResponse>
    {
    private ICreateUserCallback  mCallback;


    AddUserResponseCallback( ICreateUserCallback callback )
      {
      mCallback = callback;
      }


    @Override
    public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response )
      {
      setCallInProgress( false );

      if ( response.isSuccessful() )
        {
        CreateUserResponse createUserResponse = response.body();

        CreateUserResponse.State createUserResponseState = createUserResponse.getState();

        if ( createUserResponseState != null )
          {
          int stateCode = createUserResponseState.getCode();

          if ( stateCode == 200 )
            {
            CreateUserResponse.Data createUserResponseData = createUserResponse.getData();

            if ( createUserResponseData != null )
              {
              String userToken = createUserResponseData.getUserToken();

              if ( userToken != null )
                {
                mUserToken = userToken;

                Log.d( LOG_TAG, "Got user token: " + userToken );

                mCallback.onCreateUserSuccess( userToken );
                }
              else
                {
                returnError( "No user token returned from create user" );
                }
              }
            else
              {
              returnError( "No data returned from create user" );
              }
            }
          else
            {
            returnError( "Unable to create user: " + stateCode + " - " + createUserResponseState.getMessage() );
            }
          }
        else
          {
          returnError( "No state returned from create user" );
          }
        }
      else
        {
        returnError( "Create user returned invalid response: " + response.message() );
        }
      }


    private void returnError( String message )
      {
      Log.e( LOG_TAG, message );

      mCallback.onCreateUserFailure( message );
      }


    @Override
    public void onFailure(Call<CreateUserResponse> call, Throwable t )
      {
      setCallInProgress( false );

      returnError( t.getMessage() );
      }
    }


  /*****************************************************
   *
   * Listens for an log-in response.
   *
   *****************************************************/
  public class LogInResponseCallback implements Callback<LogInResponse>
    {
    private ILogInCallback  mCallback;


    LogInResponseCallback( ILogInCallback callback )
      {
      mCallback = callback;
      }


    @Override
    public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response )
      {
      setCallInProgress( false );

      if ( response.isSuccessful() )
        {
        LogInResponse logInResponse = response.body();

        CreateUserResponse.State createUserResponseState = logInResponse.getState();

        if ( createUserResponseState != null )
          {
          int stateCode = createUserResponseState.getCode();

          if ( stateCode == 200 )
            {
            CreateUserResponse.Data createUserResponseData = logInResponse.getData();

            if ( createUserResponseData != null )
              {
              String userToken = createUserResponseData.getUserToken();

              if ( userToken != null )
                {
                mUserToken = userToken;

                Log.d( LOG_TAG, "Got user token: " + userToken );

                mCallback.onLogInSuccess( userToken );
                }
              else
                {
                returnError( "No user token returned from create user" );
                }
              }
            else
              {
              returnError( "No data returned from create user" );
              }
            }
          else
            {
            returnError( "Unable to create user: " + stateCode + " - " + createUserResponseState.getMessage() );
            }
          }
        else
          {
          returnError( "No state returned from create user" );
          }
        }
      else
        {
        returnError( "Create user returned invalid response: " + response.message() );
        }
      }


    private void returnError( String message )
      {
      Log.e( LOG_TAG, message );

      mCallback.onLogInFailure( message );
      }


    @Override
    public void onFailure(Call<LogInResponse> call, Throwable t )
      {
      setCallInProgress( false );

      returnError( t.getMessage() );
      }
    }

  }

