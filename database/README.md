1. Heu de crear la BD amb nom: impulsfp
2. Des de psql, executar el script init.sql per crear la taula i inserir les dades inicials, així:
       psql -U postgres -d impulsfp -f database/init.sql
3. Configurar application.properties del projecte Spring Boot amb les credencials de la BD:
       spring.datasource.url=jdbc:postgresql://localhost:5432/impulsfp
       spring.datasource.username=xxxxx (aquí el vostre usuari de postgres)
       spring.datasource.password=xxxxx (aquí la vostra contrasenya de postgres)
4. Ejecutar el projecte Spring Boot per iniciar el servidor i connectar-se a la BD

És important que tingueu instal·lat el jdk 21 per poder executar el projecte, si no, es possible que no us
deixi executar-lo o que tingueu problemes de compatibilitat