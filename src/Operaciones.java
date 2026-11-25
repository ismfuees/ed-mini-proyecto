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
            
            if(id==null)return null;    // Evita llamar a equals sobre un id nulo y devuelve null si no hay id
            
            for(Persona p:e.personas.conjunto) // Recorre todas las personas almacenadas en el HashSet (e.personas.conjunto). 
                if(p.id.equals(id)) 
                    return p;   //Si el código de la persona coincide con el id buscado, retorna esa persona
            
            return null;    //Si no se encontró ninguna coincidencia tras recorrer el conjunto, retorna null
        }

        /*****************************************/
        /************** Fin HashSet **************/
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
        
        // TODO: contar PERSONAS únicas por país; incluir quienes no tengan inscripción.
        // El mapa devuelto es un resultado auxiliar, no reemplaza las colecciones activas.
        throw pendiente(e,"contarPersonasPorPais");
    }
    
    
    
    
    
    
}
