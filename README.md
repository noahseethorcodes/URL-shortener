# URL-shortener
A URL shortener RESTful webservice written in Java using the Springboot framework.
This was my first project in Java, and I wanted to build this to demonstrate my proficiency in the framework and language, as well as apply my understanding of how to build a good RESTful webservice. The project idea takes reference from a [similar Python FastAPI project](https://realpython.com/build-a-python-url-shortener-with-fastapi/) I had done previously.

Project Features include:
* Graceful Forwarding - Check if the website exists before forwarding to target URL
* Input Validation and Customised Exception Handling
* HATEOAS Implementation
* Unit and Integration Testing

## Endpoint Schema
| Endpoint            | HTTP Verb | Request Body      | Action                                                                              |
|---------------------|-----------|-------------------|-------------------------------------------------------------------------------------|
| /addURL             | POST      | User's target URL | Shows the created URL key with additional info,  including an admin key             |
| /peek_{url_key}     | GET       |                   | Returns a string showing the target URL behind the given URL key                    |
| /goto_{url_key}     | GET       |                   | Redirects to the target URL behind the given URL key                                |
| /admin/{secret_key} | GET       |                   | Shows administrative info about the shortened URL in the form of the created URLobj |
| /admin/{secret_key} | DELETE    |                   | Deletes shortened URL                                                               |

## Request Examples
### POST /addURL
<img width="1041" alt="Screenshot 2023-10-13 at 4 44 20 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/2c5bd0cf-2112-4a67-9ef6-7f71eddebbc1">

### GET /peek_{url_key}

<img width="1038" alt="Screenshot 2023-10-13 at 4 46 18 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/9e823639-5b17-4f6f-9523-81e423e8b2a8">


### GET /goto_{url_key} 
Redirects to the target URL (which in this case is https://github.com/). On Postman, the source code is returned.

<img width="1041" alt="Screenshot 2023-10-13 at 4 47 17 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/9916b273-878d-4b24-a3e3-76ef58b74b65">

### GET /admin/{secret_key} 
<img width="1029" alt="Screenshot 2023-10-13 at 4 49 40 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/25508807-1955-4578-85b9-65f86bd5e8e8">

### DELETE /admin/{secret_key} 
<img width="1039" alt="Screenshot 2023-10-13 at 4 50 07 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/a3c1053a-7ffc-4443-b01b-c94ea2f63a11">

## Exception Examples
### Invalid target URL in Request Body
<img width="1029" alt="Screenshot 2023-10-13 at 4 51 38 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/9c33e46a-198e-4cec-b3d5-e6217a58b102">

### Invalid URL key
<img width="1035" alt="Screenshot 2023-10-13 at 4 54 35 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/98e9afed-b1c7-48d4-bad3-20f96aa80819">

### Deleted URL key
This is a valid URL key that was produced by an earlier POST request. However, after being deleted, it is no longer recognised as an active URL key.

<img width="1040" alt="Screenshot 2023-10-13 at 4 55 54 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/79b95155-5747-4276-a701-479017544e5b">

### Website not found (Graceful Forwarding)
On the ocassion that a URL has been shortened but upon accessing it, the program could not receive a succssful HTTP Response Code, this exception will be thrown.

<img width="1036" alt="Screenshot 2023-10-13 at 4 59 56 PM" src="https://github.com/noahseethorcodes/URL-shortener/assets/58124018/09109dfd-095e-43e8-87b9-bf76cbed45a9">

## Potential Next Steps
* Add option for users to submit their own preferred alphanumeric URL key as well
* Host the webservice online
* Adding a frontend
