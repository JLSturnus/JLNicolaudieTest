/*****************************************************
 *
 * MainActivity.java
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

package jl.nicolaudietest;


///// Import(s) /////

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import jl.nicolaudietest.api.NicolaudieAPIAgent;


///// Class Declaration /////

/*****************************************************
 *
 * This class is the main activity.
 *
 *****************************************************/
public class MainActivity extends AppCompatActivity
  {
  ////////// Static Constant(s) //////////

  @SuppressWarnings( "unused" )
  static private final String  LOG_TAG = "MainActivity";


  ////////// Static Variable(s) //////////


  ////////// Member Variable(s) //////////

  private EditText            mEmailEditText;
  private EditText            mPasswordEditText;
  private EditText            mConfirmEditText;
  private EditText            mNameEditText;

  private ViewGroup           mConfirmLayout;
  private ViewGroup           mNameLayout;

  private TextView            mAccountModeTextView;
  private TextView            mActionDescriptionTextView;

  private AccountMode         mAccountMode;

  private NicolaudieAPIAgent  mAPIAgent;


  ////////// Static Initialiser(s) //////////


  ////////// Static Method(s) //////////


  ////////// Constructor(s) //////////


  ////////// Activity Method(s) //////////

  /*****************************************************
   *
   * Called when the activity is created.
   *
   *****************************************************/
  @Override
  public void onCreate (Bundle savedInstanceState )
    {
    super.onCreate(savedInstanceState);


    // Set up the screen

    setContentView( R.layout.activity_main );

    int entryFieldValueColour = getResources().getColor( R.color.login_box_entry_field_value_text );

    mEmailEditText             = findEditTextSetColour( R.id.email_edit_text,    entryFieldValueColour );
    mPasswordEditText          = findEditTextSetColour( R.id.password_edit_text, entryFieldValueColour );
    mConfirmEditText           = findEditTextSetColour( R.id.confirm_edit_text,  entryFieldValueColour );
    mNameEditText              = findEditTextSetColour( R.id.name_edit_text,     entryFieldValueColour);

    mConfirmLayout             = (ViewGroup)findViewById( R.id.confirm_layout );
    mNameLayout                = (ViewGroup)findViewById( R.id.name_layout );

    mAccountModeTextView       = (TextView)findViewById( R.id.account_mode_text_view );
    mActionDescriptionTextView = (TextView)findViewById( R.id.action_description_text_view );


    mAccountMode = AccountMode.CREATE;
    }


  ////////// Method(s) //////////

  /*****************************************************
   *
   * Finds an edit text and sets its underline color.
   *
   *****************************************************/
  private EditText findEditTextSetColour( int resourceId, int colour )
    {
    EditText editText = (EditText)findViewById( resourceId );

    editText.getBackground().setColorFilter( colour, PorterDuff.Mode.SRC_IN);

    return ( editText );
    }


  /*****************************************************
   *
   * Called when the existing account button is clicked.
   *
   *****************************************************/
  public void onAccountModeClicked( View view )
    {
    switch ( mAccountMode )
      {
      case CREATE:

        // The current mode is CREATE, so we need to switch to EXISTING

        setAccountMode( AccountMode.EXISTING );

        break;

      case EXISTING:

        // The current mode is EXISTING, so we need to switch to CREATE

        setAccountMode( AccountMode.CREATE );

        break;
      }
    }


  /*****************************************************
   *
   * Sets a new mode.
   *
   *****************************************************/
  private void setAccountMode( AccountMode newAccountMode )
    {
    switch ( newAccountMode )
      {
      case CREATE:

        // Show the hidden fields
        mConfirmLayout.setVisibility( View.VISIBLE );
        mNameLayout.setVisibility( View.VISIBLE );

        // Change the button text
        mAccountModeTextView.setText( R.string.login_existing_account );
        mActionDescriptionTextView.setText( R.string.login_action_create );

        break;


      case EXISTING:

        // Hide the unwanted fields
        mConfirmLayout.setVisibility( View.INVISIBLE );
        mNameLayout.setVisibility( View.INVISIBLE );

        // Change the button text
        mAccountModeTextView.setText( R.string.login_create_account );
        mActionDescriptionTextView.setText( R.string.login_action_login );

        break;

      }

    mAccountMode = newAccountMode;
    }


  /*****************************************************
   *
   * Called when the action button is clicked.
   *
   *****************************************************/
  public void onActionClicked( View view )
    {
    if ( getAPIAgent().isCallInProgress() ) return;

    String email;
    String password;
    String password2;
    String name;

    switch ( mAccountMode )
      {
      case CREATE:

        // Get the fields
        email     = mEmailEditText.getText().toString();
        password  = mPasswordEditText.getText().toString();
        password2 = mConfirmEditText.getText().toString();
        name      = mNameEditText.getText().toString();

        // Validate the fields
        if ( ( email = emailIsOK( email ) ) == null ) return;
        if ( ! passwordIsOK( password ) ) return;
        if ( ! passwordsMatch( password, password2 ) ) return;

        Pair<String,String> firstSecondNames;

        if ( ( firstSecondNames = nameIsOK( name ) ) == null ) return;

        createAccount( email, password, firstSecondNames );

        break;

      case EXISTING:

        // Get the fields
        email     = mEmailEditText.getText().toString();
        password  = mPasswordEditText.getText().toString();

        // Validate the fields
        if ( ( email = emailIsOK( email ) ) == null ) return;
        if ( ! passwordIsOK( password ) ) return;

        logIn( email, password );

        break;
      }
    }


  /*****************************************************
   *
   * Validates the email address. Displays a message if
   * not.
   *
   * @return true, if the email is OK
   * @return false, otherwise
   *
   *****************************************************/
  private String emailIsOK( String email )
    {
    if ( email == null || email.length() == 0 || ( email = email.trim() ).length() == 0 )
      {
      displayError( "Please enter a valid email address" );

      return ( null );
      }

    return ( email );
    }


  /*****************************************************
   *
   * Validates the password. Displays a message if
   * not.
   *
   * @return true, if the password is OK
   * @return false, otherwise
   *
   *****************************************************/
  private boolean passwordIsOK( String password )
    {
    if ( password == null || password.length() == 0 || password.trim().length() == 0 )
      {
      displayError( "Please enter a valid password" );

      return ( false );
      }

    return ( true );
    }


  /*****************************************************
   *
   * Validates the name and returns the first / second names.
   * Displays a message if not.
   *
   * We allow just a first name.
   *
   * @return The first and second names, if the name is OK
   * @return null, otherwise
   *
   *****************************************************/
  private Pair<String,String> nameIsOK( String name )
    {
    if ( name == null || name.length() == 0 || ( name = name.trim() ).length() == 0 )
      {
      displayError( "Please enter a valid name" );

      return ( null );
      }


    // Find the end of the first name

    int endIndex = name.indexOf( ' ' );

    // If there is no second name, just return the first
    if ( endIndex < 0 )
      {
      return (new Pair<String, String>(name, ""));
      }


    return ( new Pair<String,String>( name.substring(0, endIndex ), name.substring( endIndex ).trim() ) );
    }


  /*****************************************************
   *
   * Validates that the passwords match.
   *
   * @return true, if the passwords match OK
   * @return false, otherwise
   *
   *****************************************************/
  private boolean passwordsMatch( String password1, String password2 )
    {
    if ( password1 == null || password2 == null || ! password1.equals( password2 ) )
      {
      displayError( "The passwords do not match" );

      return ( false );
      }

    return ( true );
    }


  /*****************************************************
   *
   * Displays an error message.
   *
   *****************************************************/
  private void displayError( String message )
    {
    new AlertDialog.Builder( this )
            .setTitle( message )
            .setCancelable( true )
            .setPositiveButton( "OK", null )
            .create()
            .show();
    }


  /*****************************************************
   *
   * Creates a new account.
   *
   *****************************************************/
  private void createAccount( String email, String password, Pair<String,String> firstAndSecondNames )
    {
    getAPIAgent().createAccount( email, password, firstAndSecondNames, new CreateUserListener() );
    }


  /*****************************************************
   *
   * Logs in to an existing account.
   *
   *****************************************************/
  private void logIn( String email, String password )
    {
    getAPIAgent().logIn( email, password, new LogInListener() );
    }


  /*****************************************************
   *
   * Returns an instance of the API agent.
   *
   *****************************************************/
  private NicolaudieAPIAgent getAPIAgent()
    {
    if ( mAPIAgent == null )
      {
      mAPIAgent = new NicolaudieAPIAgent( this );
      }

    return ( mAPIAgent );
    }


  /*****************************************************
   *
   * Displays a logged in message.
   *
   *****************************************************/
  private void onLoggedIn( String title, String userToken )
    {
    // Switch to EXISTING mode
    setAccountMode( AccountMode.EXISTING );

    // Clear all fields except email
    mPasswordEditText.setText( "" );
    mConfirmEditText.setText( "" );
    mNameEditText.setText( "" );


    // Inform user, and provide log out button

    new AlertDialog.Builder( this )
            .setTitle( title )
            .setMessage( "User token: " + userToken )
            .setCancelable( false )
            .setPositiveButton("Log out", new DialogInterface.OnClickListener()
              {
              @Override
              public void onClick( DialogInterface dialogInterface, int which )
                {
                getAPIAgent().logOut();
                }
              })
            .create()
            .show();
    }


  ////////// Inner Class(es) //////////

  /*****************************************************
   *
   * The mode: create account or existing account.
   *
   *****************************************************/
  private enum AccountMode
    {
    CREATE,
    EXISTING
    }


  /*****************************************************
   *
   * The create user callback.
   *
   *****************************************************/
  private class CreateUserListener implements NicolaudieAPIAgent.ICreateUserCallback
    {
    @Override
    public void onCreateUserFailure(String message)
      {
      displayError( message );
      }

    @Override
    public void onCreateUserSuccess(String userToken)
      {
      onLoggedIn( "User Created", userToken );
      }
    }


  /*****************************************************
   *
   * The log in callback.
   *
   *****************************************************/
  private class LogInListener implements NicolaudieAPIAgent.ILogInCallback
    {
    @Override
    public void onLogInFailure(String message)
      {
      displayError( message );
      }

    @Override
    public void onLogInSuccess(String userToken)
      {
      onLoggedIn( "User Logged In", userToken );
      }
    }

  }

