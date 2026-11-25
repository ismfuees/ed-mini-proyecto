import java.util.*;

/** ArrayList ya funciona. Implementen las cinco operaciones para las otras tres estructuras elegidas. */
public final class Operaciones 
{
    private Operaciones() {}
    private static UnsupportedOperationException pendiente(Estado e,String metodo) 
    {
        return new UnsupportedOperationException("TODO: "+metodo+" para "+e.tipo);
    }
    
    public static Persona buscarPersona(Estado e,String id) 
    {
        if(e.tipo==Estado.Tipo.ArrayList) 
        {
            for(Persona p:e.personas.lista)if(p.id.equals(id))return p;
            return null;
        }
        
        /*****************************************/
        /************ Inicio HashSet *************/
        /*****************************************/
        
        /*****************************************/
        /************** Fin HashSet **************/
        /*****************************************/
        
        if(e.tipo==Estado.Tipo.HashSet)     // Comprueba si la estructura activa en el estado es HashSet
        {
            
            if(id==null)return null;     // Evita llamar a equals sobre un id nulo y devuelve null si no hay id
            
            for(Persona p:e.personas.conjunto) // Recorre todas las personas almacenadas en el HashSet (e.personas.conjunto). 
                if(p.id.equals(id)) 
                    return p;   //Si el código de la persona coincide con el id buscado, retorna esa persona
            
            return null;    //Si no se encontró ninguna coincidencia tras recorrer el conjunto, retorna null
        }

        /*****************************************/
        /************** Fin HashSet **************/
        /*****************************************/

        /*****************************************/
        /************ Inicio HashMap *************/
        /*****************************************/

        if(e.tipo==Estado.Tipo.HashMap) // Revisamos si el programa está trabajando con HashMap
        {
            if(id==null) // Si nos pasan un id nulo, salimos de una vez
                return null; // Devolvemos null sin error para no romper la búsqueda

            return e.personas.mapa.get(id); // Buscamos directo por clave (id) en el mapa y devolvemos la Persona (o null si no está)
        }

        /*****************************************/
        /************** Fin HashMap **************/
        /*****************************************/
    
        // TODO: resolver con la estructura activa elegida. Devolver null si no existe.
        throw pendiente(e,"buscarPersona");
         
    }
    
    public static boolean corregirPais(Estado e,String idPersona,String codigoPais) {
        if(e.tipo==Estado.Tipo.ArrayList) {
            boolean existePais=false;
            for(Pais p:e.paises.lista)if(p.id.equals(codigoPais)){existePais=true;break;}
            if(!existePais)return false;
            for(int i=0;i<e.personas.lista.size();i++) {
                Persona p=e.personas.lista.get(i);
                if(p.id.equals(idPersona)) {
                    if(p.codigoPais.equals(codigoPais))return false;
                    e.personas.lista.set(i,new Persona(p.id,p.nombre,codigoPais));return true;
                }
            }
            return false;
        }
        
        /********************************************/
        /************** Inicio HashSet **************/
        /********************************************/

        if(e.tipo==Estado.Tipo.HashSet)     // Verifica si la estructura activa en el estado es HashSet
        {
            
            if(idPersona==null || codigoPais==null)     // Devuelve false si idPersona o codigoPais son nulos para evitar errores
                return false;

            boolean existePais=false;   // Bandera para saber si el código de país destino existe.
            
            for(Pais pais:e.paises.conjunto)    // Recorre el conjunto de países (e.paises.conjunto) buscando el código solicitado
            {
                
                if(pais.id.equals(codigoPais))  // Marca que el país sí existe y detiene la búsqueda
                {
                    existePais=true;
                    break;
                }
            }
            
            if(!existePais)     // Si el país es destino o no existe, no altera datos y retorna false
                return false;

            Persona actual=null;    // Guarda la instancia actual de la persona si se encuentra en el conjunto
            
            for(Persona persona:e.personas.conjunto)    // Recorre las personas del HashSet buscando la que tenga idPersona
                if(persona.id.equals(idPersona))    // Guarda la referencia encontrada y termina el recorrido
                {
                    actual=persona;
                    break;
                }
            
            if(actual==null || actual.codigoPais.equals(codigoPais))    // Si no existe la persona o ya pertenece a ese país, retorna false
                return false;

            e.personas.conjunto.remove(actual); // Quita la instancia anterior porque Persona es inmutable y equals/hashCode dependen del id
            
            e.personas.conjunto.add(new Persona(actual.id,actual.nombre,codigoPais)); // Agrega una nueva Persona con el país actualizado conservando id y nombre
            
            return true;    // Indica que el cambio de país se realizó correctamente
        }
        
        /*****************************************/
        /************** Fin HashSet **************/
        /*****************************************/

        /*****************************************/
        /************ Inicio HashMap *************/
        /*****************************************/

        if(e.tipo==Estado.Tipo.HashMap) // Revisamos si la estructura activa es HashMap
        {
            if(idPersona==null || codigoPais==null) // Verificamos que tanto el id de la persona como el código de país existan (no sean null)
                return false; // Si falta algún dato, rechazamos el cambio y devolvemos false

            if(!e.paises.mapa.containsKey(codigoPais)) // Comprobamos si el país destino está registrado en el mapa de países
                return false; // Si el país no existe, no tocamos nada y devolvemos false

            Persona actual=e.personas.mapa.get(idPersona); // Consultamos a la persona actual usando su id como clave en el mapa
            if(actual==null || actual.codigoPais.equals(codigoPais)) // Revisamos que la persona exista y que de verdad vaya a cambiar de país
                return false; // Si no existe o ya tiene ese mismo país, devolvemos false sin hacer cambios

            e.personas.mapa.put(idPersona,new Persona(actual.id,actual.nombre,codigoPais)); // Como Persona es inmutable, guardamos una copia nueva con el país corregido manteniendo id, nombre e inscripciones
            return true; // Avisamos que el país se actualizó bien
        }

        /*****************************************/
        /************** Fin HashMap **************/
        /*****************************************/
        
        // TODO: validar país/persona y reemplazar la persona manteniendo su identidad.
        throw pendiente(e,"corregirPais");
    }
    
    public static boolean registrarInscripcion(Estado e,String codigo,String idPersona,String actividad) {
        
        if(e.tipo==Estado.Tipo.ArrayList) 
        {
            if(codigo==null || codigo.trim().isEmpty() || !Datos.ACTIVIDADES.contains(actividad))return false;
            if(buscarPersona(e,idPersona)==null)return false;
            for(Inscripcion i:e.inscripciones.lista)
                if(i.id.equals(codigo) || (i.idPersona.equals(idPersona) && i.actividad.equals(actividad)))return false;
            e.inscripciones.lista.add(new Inscripcion(codigo,idPersona,actividad));return true;
        }

        /********************************************/
        /************** Inicio HashSet **************/
        /********************************************/

        if(e.tipo==Estado.Tipo.HashSet)     // Verifica si la estructura activa en el estado es HashSet
        {
            
            if(codigo==null || codigo.trim().isEmpty() || actividad==null || !Datos.ACTIVIDADES.contains(actividad))    // Rechaza si el código es nulo o vacío, o si la actividad no existe en Datos.ACTIVIDADES
                return false;
            
            if(idPersona==null || buscarPersona(e,idPersona)==null)     // Comprueba que la persona exista antes de inscribirla; si no existe, devuelve false
                return false;
            
            for(Inscripcion i:e.inscripciones.conjunto)     // Recorre las inscripciones guardadas en e.inscripciones.conjunto
                if(i.id.equals(codigo) || (i.idPersona.equals(idPersona) && i.actividad.equals(actividad)))     // Impide códigos duplicados o que la misma persona se inscriba dos veces en la misma actividad
                    return false;
            
            e.inscripciones.conjunto.add(new Inscripcion(codigo,idPersona,actividad));      // Agrega únicamente la nueva inscripción al HashSet sin duplicar Persona ni Pais
            
            return true;    // Devuelve true indicando que se registró la inscripción
        }
        
        /*****************************************/
        /************** Fin HashSet **************/
        /*****************************************/

        /*****************************************/
        /************ Inicio HashMap *************/
        /*****************************************/

        if(e.tipo==Estado.Tipo.HashMap) // Revisamos si la estructura activa es HashMap
        {
            if(codigo==null || codigo.trim().isEmpty() || actividad==null || !Datos.ACTIVIDADES.contains(actividad)) // Validamos que el código no esté vacío y que la actividad sea una de las permitidas
                return false; // Si el código o la actividad no son válidos, no registramos nada

            if(idPersona==null || buscarPersona(e,idPersona)==null) // Verificamos que la persona exista antes de inscribirla
                return false; // Si la persona no existe, devolvemos false sin crear personas ni países

            if(e.inscripciones.mapa.containsKey(codigo)) // Revisamos si ya existe una inscripción con ese mismo código
                return false; // No permitimos códigos de inscripción repetidos

            for(Inscripcion ins:e.inscripciones.mapa.values()) // Recorremos las inscripciones actuales para revisar duplicados de persona + actividad
                if(ins.idPersona.equals(idPersona) && ins.actividad.equals(actividad)) // Si la persona ya está inscrita en esa actividad...
                    return false; // ...rechazamos la inscripción duplicada

            e.inscripciones.mapa.put(codigo,new Inscripcion(codigo,idPersona,actividad)); // Guardamos solo la nueva inscripción en el mapa usando su código como clave
            return true; // Confirmamos que la inscripción quedó registrada
        }

        /*****************************************/
        /************** Fin HashMap **************/
        /*****************************************/
        
        // TODO: validar persona, código único, actividad válida y combinación persona/actividad única.
        // Agregar solo la inscripción: no duplicar Persona ni Pais.
        throw pendiente(e,"registrarInscripcion");
    }
    
    public static boolean cancelarInscripcion(Estado e,String codigo) {
        if(e.tipo==Estado.Tipo.ArrayList) {
            Iterator<Inscripcion> it=e.inscripciones.lista.iterator();
            while(it.hasNext())if(it.next().id.equals(codigo)){it.remove();return true;}
            return false;
        }
        
        
        /********************************************/
        /************** Inicio HashSet **************/
        /********************************************/
       
        if(e.tipo==Estado.Tipo.HashSet)     // Verifica si la estructura activa en el estado es HashSet
        {
            
            if(codigo==null)    // Si el código es nulo, no hay inscripción que cancelar y devuelve false.
                return false;
            
            Iterator<Inscripcion> it=e.inscripciones.conjunto.iterator();   // Obtiene un Iterator para eliminar con seguridad mientras recorre el HashSet
            
            while(it.hasNext()) 
            {
                Inscripcion ins=it.next();  // Obtiene la siguiente inscripción del conjunto
                
                if(ins.id.equals(codigo))      // Si coincide el código, elimina solo la inscripción y conserva persona y país
                {
                    it.remove();
                    return true;
                }
            }
            // Devuelve false si no se encontró ninguna inscripción con ese código.
            return false;
        }
        
        /*****************************************/
        /************** Fin HashSet **************/
        /*****************************************/

        /*****************************************/
        /************ Inicio HashMap *************/
        /*****************************************/

        if(e.tipo==Estado.Tipo.HashMap) // Revisamos si la estructura activa es HashMap
        {
            if(codigo==null) // Si no llega un código válido (es null), no hay nada que borrar
                return false; // Devolvemos false sin tocar el mapa

            return e.inscripciones.mapa.remove(codigo)!=null; // Quitamos solo la inscripción del mapa (sin borrar ni a la persona ni al país) y devolvemos true si existía
        }

        /*****************************************/
        /************** Fin HashMap **************/
        /*****************************************/

        // TODO: eliminar solo la inscripción. Conservar persona y país, aun sin otras inscripciones.
        throw pendiente(e,"cancelarInscripcion");
    }

    public static Map<String,Integer> contarPersonasPorPais(Estado e) {
        if(e.tipo==Estado.Tipo.ArrayList) {
            Map<String,Integer> resultado=new TreeMap<>();
            for(Pais pais:e.paises.lista)resultado.put(pais.id,0);
            for(Persona persona:e.personas.lista)
                resultado.put(persona.codigoPais,resultado.get(persona.codigoPais)+1);
            return resultado;
        }
        
        /********************************************/
        /************** Inicio HashSet **************/
        /********************************************/
        
        if (e.tipo == Estado.Tipo.HashSet) // Verifica si la estructura activa en el estado es HashSet
        {
            
            Map<String, Integer> resultado = new TreeMap<>();   // Crea un mapa ordenado por código de país para guardar el conteo final
            
            for (Pais pais : e.paises.conjunto) // Inicializa cada país de e.paises.conjunto con 0 para incluir países sin personas
                resultado.put(pais.id, 0);
            
            for (Persona persona : e.personas.conjunto)     // Recorre cada persona en e.personas.conjunto para contabilizarla en su país
            {
                Integer actual = resultado.get(persona.codigoPais);// Obtiene el acumulado actual del país de la persona
                
                if (actual != null) 
                    resultado.put(persona.codigoPais, actual + 1);  // Si el código de país existe en el mapa, incrementa su contador en 1
            }
            
            return resultado;   // Devuelve el mapa auxiliar sin modificar las colecciones activas
        }
        
        
        /*****************************************/
        /************** Fin HashSet **************/
        /*****************************************/

        /*****************************************/
        /************ Inicio HashMap *************/
        /*****************************************/

        if(e.tipo==Estado.Tipo.HashMap) // Revisamos si la estructura activa es HashMap
        {
            Map<String,Integer> resultado=new TreeMap<>(); // Creamos un mapa auxiliar ordenado para devolver el conteo final por país

            for(String codPais:e.paises.mapa.keySet()) // Recorremos todos los códigos de país registrados
                resultado.put(codPais,0); // Arrancamos cada país en 0, así también aparecen países con 0 personas

            for(Persona persona:e.personas.mapa.values()) // Recorremos cada persona única (sin importar cuántas inscripciones tenga o si no tiene ninguna)
            {
                Integer actual=resultado.get(persona.codigoPais); // Tomamos el conteo que lleva ese país hasta ahora
                if(actual!=null) // Si el país de la persona está en nuestro mapa de resultados...
                    resultado.put(persona.codigoPais,actual+1); // ...le sumamos 1 persona
            }

            return resultado; // Entregamos el mapa con los totales sin modificar las colecciones del programa
        }

        /*****************************************/
        /************** Fin HashMap **************/
        /*****************************************/
        
        // TODO: contar PERSONAS únicas por país; incluir quienes no tengan inscripción.
        // El mapa devuelto es un resultado auxiliar, no reemplaza las colecciones activas.
        throw pendiente(e,"contarPersonasPorPais");
    }
}