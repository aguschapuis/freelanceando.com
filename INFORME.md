# Informe de Lab 2 Paradigmas | Grupo 24

## Integrantes:
- Martín Maximiliano Roberto Monnittola
- Ariel Anelio Alvarez Godoy
- Agustin Chapuis

## Breve Resumen:


### Trait Model

Se definio los siguientes metodos del trait Model:

```matchWithFilters:```  toma como parametro de entrada un diccionario y retoran un Booleano, el 
                   diccionario de entrada consta de las asociasiones entre los atributos y 
                   los valores de los mismo con los que se determina si el objeto sobre el
                   que es llamado el metodo cumple o no.

```validateKeys:```  toma como parametro de entrada un conjunto de String y no retorna valor alguno .
               La tarea de este procedimiento es controlar que los elementos del conjunto tomado
               como entrada sean todos nombres de algun atributo miembro de la clase excepto el 
               atributo 'id'.



### Class Client

Se definio la clase cliente la cual contiene los atributos y metodos necesarios para poder
contener los datos del usuario cliente y los metodos necesarios para operar sobre estos datos.

Se definieron los siguientes atributos:

```username:``` representa el nombre del usuario
```country_code:``` representa el codigo del pais
```total_spend:``` representa el dinero total gastado

Los metodos definidos fueron:

```getUsername:``` para obtener el nombre del cliente
```getCountry_code:``` para obtener el codigo del pais
```getTotal_spend:``` para obtener el total gastado hasta el momento

```fromJson:``` Se sobreescribio el metodo fromJson heredado del trait model, debido a que
          la Clase Client tenia atributos propios no definidos en Model, pero se lo llamo
          (al metodo de Model) para que procesara los atributos definidos en dicho Trait, esto
          se decidio asi porque consideramos que no se deberia reescribir codigo y tambien
          porque pensamos que el procedimiento que procese dichos datos debia realizar la tarea
          donde se definieron esos atributos.

```toMap:``` Se sobreescribio el metodo heredado de Model, debido a que se debia contemplar los
        atributos de la Clase, pero se lo llamo para que procesara los atributos que se heredaban
        de Model.

```validateKeys:``` toma un conjunto de String y controla que todos sus elementos sean nombres de
               atributos de la Clase Client.

```IncrementTotal_spend:``` Toma un entero como parametro de entrada y con este incrementa el valor
               del atributo Total_spend. Se decidio crear el metodo para mantener en 

### Class Job

Se definio la clase Job la cual contiene los atributos y metodos necesarios para poder
contener los datos de cada trabajo y los metodos necesarios para operar sobre estos datos.

Atributos:

```title:``` Titulo del trabajo
```category:``` Id de la categoria del trabajo
```client_id:```  Numero de id del cliente
```preferred_expertise:``` Experiencia que prefiere el cliente que publica el trabajo
```preferred_country:``` Pais que prefiere el cliente que publica el trabajo
```hourly_price:``` Precio que se va a pagar por este trabajo (por hora)

Metodos definidos:

```validateCategoryId:``` Metodo que valida que el id de la categoria del trabajo exista entre las categorias ya definidas 

```validateClientId:``` Metodo que controla que el cliente que ofrece el trabajo exista entre los clientes ya registrados en la pagina

### Preguntas de la catedra:

Mencionen y expliquen al menos dos casos en los que utilizaron herencia para evitar tener que duplicar código.
¿En qué clase se encargan de controlar que las categorías asignadas a un freelancer efectivamente existan? ¿Por qué es responsabilidad de esa clase y no de otra?

1- Los metodos de fromJson y toMap son dos casos donde se uso la herencia para evitar la dulicacion de codigo
ya que a pesar de que se tuvo que sobreescribir dichos metodos al implementar los requerimientos de la catedra,
se hizo una llamada a super en la clase Client y Freelance para evitar tener que escribir el codigo necesario
para manejar el atributo Id, ya sea para pasarlo a un diccionario o para crear un objeto a partir de un Json.

2- Habia dos formas de abordar este problema:
     La primera forma era crear un metodo en la Clase databaseTable para que controle que en su atributo
     ```_instances```, que es un diccionario , tenga una clave que machee con la clave ingresada para el Freelancer
     a crear.
     La segunda era listar las claves de las Categorias disponibles o los Idś en el endpoint correspondiente
     en este caso  ```post /api/freelancers```, crear el Objeto Freelancer con los valores que deeberian 
     ser categorias y llamar a un metodo definido en la Clase Freelancer que tome la lista de ids de 
     Categorias cargados en el sistema y los compare con la lista almacenada en si mismo (en una atributo 
     propio)
   
   Se eligio el punto dos para implementar nuestro codigo, pero al analizar al final lo implementado nos
   dimos cuenta que el punto uno era mejor, el tiempo no nos alcanzo para modificarlo.

¿Qué concepto de la programación orientada a objetos es el que les permite que un mismo endpoint como `api/freelancers` tome distintos parámetros?
FreelanceandoServlet

El concepto usado 


* ¿Dónde colocaron el código con la lógica de la acción pagar? ¿Lo dividieron entre varios objetos, o pusieron todo en un mismo lugar?
* ¿Dónde tendrían que agregar código si se decidiera llamar a una API externa como MercadoPago para realizar la transacción?

1- Se dividio la logica de la accion pagar en distintos objetos, primero el incremento del monto
   gastado del cliente se lo realizo creando un metodo en la clase Client,IncrementTotal_spend,el cual
   modifica el atributo total_spend, luego el incremento del monto del total ganado por el Freelancer se lo 
   realiza en la Clase Freelancer a traves del metodo IncrementTotal_earning, el cual modifica el atributo 
   total_earning. Ambos metodos son llamados desde el bloque de accion de la ruta ```post /api/posts/pay```
   para realizar el procesamiento completo.

2- Se agregaria codigo en la Clase FreelanceandoServlet para hacer el llamado, pero se deberia retocar la
   Clase Freelancer y Client para agregar miembros y metodos que soporten las cuentas y los mecanismo
   de autenticacion que exije el sitio de mercado libre.


   
     



               