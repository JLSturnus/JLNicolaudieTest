/*****************************************************
 *
 * LogInResponse.java
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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


///// Class Declaration /////

/*****************************************************
 *
 * This class holds the response values to a log-in
 * API call.
 *
 *****************************************************/
public class LogInResponse
  {
  ////////// Static Constant(s) //////////

  @SuppressWarnings( "unused" )
  static private final String  LOG_TAG = "LogInResponse";


  ////////// Static Variable(s) //////////


  ////////// Member Variable(s) //////////

  @SerializedName( "header" )
  @Expose
  private String header;

  @SerializedName( "title" )
  @Expose
  private String title;

  @SerializedName( "uri" )
  @Expose
  private String uri;

  @SerializedName( "method" )
  @Expose
  private String method;

  @SerializedName( "state" )
  @Expose
  private State state;

  @SerializedName( "data" )
  @Expose
  private Data data;


  ////////// Static Initialiser(s) //////////


  ////////// Static Method(s) //////////


  ////////// Constructor(s) //////////


  ////////// Method(s) //////////

  /*****************************************************
   *
   * Returns the state.
   *
   *****************************************************/
  public State getState()
    {
    return ( this.state );
    }


  /*****************************************************
   *
   * Returns the data.
   *
   *****************************************************/
  public Data getData()
    {
    return ( this.data );
    }


  ////////// Inner Class(es) //////////

  /*****************************************************
   *
   * The response state.
   *
   *****************************************************/
  public class State
    {
    @SerializedName( "code" )
    @Expose
    private int code;

    @SerializedName( "msg" )
    @Expose
    private String message;

    public int getCode()
      {
      return ( this.code );
      }

    public String getMessage()
      {
      return ( this.message );
      }
    }


  /*****************************************************
   *
   * The response data.
   *
   *****************************************************/
  public class Data
    {
    @SerializedName( "user_token" )
    @Expose
    private String userToken;

    public String getUserToken()
      {
      return ( this.userToken );
      }
    }

  }

