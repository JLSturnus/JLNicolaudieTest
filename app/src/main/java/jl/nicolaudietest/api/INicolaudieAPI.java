/*****************************************************
 *
 * INicolaudieAPI.java
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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


///// Class Declaration /////

/*****************************************************
 *
 * This interface defines the Nicolaudie API.
 *
 *****************************************************/
public interface INicolaudieAPI
  {
  ////////// Constant(s) //////////


  ////////// Method(s) //////////

  /*****************************************************
   *
   * Creates a new user.
   *
   *****************************************************/
  @Headers( {"Content-Type: application/json"} )
  @POST("user/")
  Call<CreateUserResponse> addUser( @Body String body );


  /*****************************************************
   *
   * Logs an existing user in.
   *
   *****************************************************/
  @Headers( {"Content-Type: application/json"} )
  @POST("user/login")
  Call<LogInResponse> logIn( @Body String body );


  ////////// Inner Class(es) //////////

  /*****************************************************
   *
   * ...
   *
   *****************************************************/

  }