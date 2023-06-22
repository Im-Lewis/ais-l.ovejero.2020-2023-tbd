# AIS-Practica-3-2023

Autor: Luis Ovejero Martín 

[Repositorio](https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd)

[Aplicación Okteto](https://cloud.okteto.com/spaces/im-lewis?resourceId=85c859c2-eb26-45f8-9bae-8c627b925864)

## Desarrollo con (GitFlow/TBD)

### Paso 1: Definicion de workflows
Antes de empezar con el desarrollo de la nueva funcionalidad preparamos los workflows y comprobamos que funcionen. Además añadimos el "sanity test" para comprobar que la aplicación está correctamente desplegada.


### Paso 2: Desarrollo de una funcionalidad nueva 

Una vez que hemos comprobado que todos los workflows y el test funcionan, borramos las ejecuciones de los worflows que hasn sido de prueba para dejar solo las que se ejecuten durante el desarrollo de la nueva funcionalidad. Después de esto ya podemos pasar a crear la nueva funcionalidad para hacer las descripciones de 950 caracteres, utilizando (Gitflow o TBD):

Previamente hemos clonado el repositorio para poder añadir el "sanity test" con el comando: 
```
$ git clone https://github.com/Im-Lewis/ais-l.ovejero.2020-2023-tbd
```

Ahora para poder empezar a añadir la nueva funcionalidad necesitamos crear una rama nueva para trabajar en ella: 
```
$ git checkout -b feature-short-descriptions
```
Con este último comando podremos crear una rama y trabajar en ella, en esta rama es donde crearemos la nueva funcionalidad, haremos los commits correspondientes para añadir esta funcionalidad con los comandos: 
```
$ git add .
$ git commit -m "Update: Short Descriptions"
$ git push origin feature-short-descriptions
```
Con estos comandos añadimos las nuevas modificaciones que hayamos hecho a los ficheros del código, creamos la mini versión y la subimos a nuestra rama "feature-short-descriptions".
