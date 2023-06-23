# AIS-Practica-3-2023

Autor: Luis Ovejero Martín 

[Repositorio](https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd)

[Aplicación Okteto](https://books-reviewer-tbd-im-lewis.cloud.okteto.net/)

## Desarrollo con (GitFlow/TBD)

### Paso 1: Definición de workflows
Antes de empezar con el desarrollo de la nueva funcionalidad, preparamos los "workflows" y comprobamos que funcionen. Además añadimos el "sanity test" para comprobar que la aplicación está correctamente desplegada.


### Paso 2: Desarrollo de una funcionalidad nueva 
Una vez que hemos comprobado que todos los "workflows" y el test funcionan, borramos las ejecuciones de los "worflows" que han sido de prueba para dejar solo las que se ejecuten durante el desarrollo de la nueva funcionalidad. Después de esto ya podemos pasar a crear la nueva funcionalidad para hacer las descripciones de 950 caracteres, utilizando (Gitflow o TBD):

Previamente hemos clonado el repositorio para poder añadir el "sanity test" con el comando: 
```
$ git clone https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd
```
Antes de empezar a trabajar, comprobamos que estamos actualizados con el comando: 
```
$ git pull origin master 
```
De esta manera actualizamos nuestra versión local con los cambios que pudiera haber en el repositorio de github.

Ahora para poder empezar a añadir la nueva funcionalidad necesitamos crear una rama nueva para trabajar en ella: 
```
$ git checkout -b feature-short-descriptions
```
Con este último comando podremos crear una rama y empezar a trabajar en ella, esta rama es donde crearemos la nueva funcionalidad.
Antes de empezar a trabajar comprobamos que estamos en la nueva rama que acabamos de crear con el comando: 
```
$ git branch
```
Añadimos la nueva funcionalidad modificando el archivo "bookDetail", en su metodo "setDescription" comprobaremos la longitud de la descripción y la modificaremos si supera los 950 caracteres para que sea de 950 caracteres y tres puntos (...).

Ahora que hemos creado la nueva funcionalidad vamos a usar el comando: 
```
$ git status 
```
De esta manera vamos a comprobar que archivos han sido modificados. Vemos que solo se ha cambiado el que hemos modificado y usamos el comando: 
```
$ git add .
```
Con este comando podremos añadir los ficheros nuevos o los cambios realizados en los que ya había, como es nuestro caso.
Tras ejecutar este comando volvemos a usar: 
```
$ git status 
```
Vemos que el nombre de los ficheros modificados que antes estaba en rojo a cambiado a verde, eso quiere decir que sean añadido correctamente. Ahora podemos hacer el "commit" de esos archivos:
```
$ git commit -m "Update bookDetail to make short descriptions"
```
Así creamos la mini versión de nuestro código.
Finalmente para comprobar que hemos hecho el "commit" correctamente usamos:
```
$ git status 
```
Vemos que ya no aparecen el nombre de los ficheros que la última vez estaban en verde y eso quiere decir que el "commit" sea ha hecho correctamente.
Ahora podemos pasar a subir nuestra rama al repositorio de github con el comando: 
```
$ git push origin feature-short-descriptions 
```
Vamos a nuestro repositorio de github y vemos que aparece un mensaje con un botón que pone "Compare & pull request", le damos a ese botón. Añadimos una descripción y creamos el "pull request".

#### Workflow 1
Al haber hecho el "pull request" vemos que sea ha ejecutado el workflow 1, este se ejecutaba antes de integrar una rama feature en la master. Vemos que se ha ejecutado con exito.
Este sería el enlace a la ejecución de este "workflow": https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd/actions/runs/5349163801
##

Ahora desde la pagina de github podemos hacer el "merge" del "pull request" que hemos hecho con la nueva funcionalidad, de esta forma podremos añadir los cambios que hemos hecho a nuestra rama "master".

#### Workflow 2 
Tras aceptar el "pull request" y hacer el "merge" con la rama "master" vemos como salta el "workflow" 2 para ejecuatr todas las pruebas, menos el "sanity test", y vemos que se ha ejecutado con exito.
Este sería el enlace a la ejecución de este "workflow": https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd/actions/runs/5349323131
##
Por último, nos queda desplegar la versión actual de la aplicación en Okteto, para ello vamos a crear una rama release-*, donde el asterisco será el hash del "commit" que indicará la versión de la release: Usaremos los commandos: 
```
$ $commit_hash=$(git log --pretty=format:"%H" -n 1)
$ git checkout -b release-$commit_hash
$ git push origin release-$commit_hash
```
Con el primero crearemos una variable donde vamos a recoger el hash del último "commit", el segundo creará la nueva rama de "release" y pasaremos a trabajar en ella y con el tercero subiremos la rama al repositorio de github.

#### Workflow 3 
Al subir esta rama de "release" se ejecutará el workflow 3, vemos que se ejecuta satisfactoriamente. 
Este sería el enlace a la ejecución de este workflow: https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd/actions/runs/5349701118
###
En este caso también generamos una imagen de docker cuyo enlace es: https://hub.docker.com/layers/imlewiis/books-reviewer/31050df0c83395eae5dbc55c960ded3e18b0b49b/images/sha256-4962a7978834d7c1f0f7d1595f4868327414c2f6315f24d52d71284ba093b341?context=repo 
##
A la hora de crear la rama de release tuve un problema a la hora de nombrarla con el hash del último "commit" y cree una rama llamada "release-", como era un error la borré junto con la ejecución del "workflow" 3 que se hizo al crearla. La creé de nuevo de la forma correcta y esa es la que está activa con su correspondiente ejecución del workflow.
##
Aquí podemos ver la aplicación desplegada con el libro "Alice's Adventures in Wonderland / Through the Looking Glass" abierto:
###
![image](https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd/assets/118623973/12b69370-2eff-4405-9430-717f8a54fa6d)
###
##
#### Workflow 4 
Este "workflow" esta programado para ejecutarse por la noche, sobre las 4 AM hora española. Su función es ejecutar los test de la aplicación, crear y subir uan imagen docker.
Este sería el enlace a la ejecución de este "workflow": https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd/actions/runs/5352371921
##
Y este a la imagen generada: https://hub.docker.com/layers/imlewiis/books-reviewer/dev-20230623.025730./images/sha256-4962a7978834d7c1f0f7d1595f4868327414c2f6315f24d52d71284ba093b341?context=repo

