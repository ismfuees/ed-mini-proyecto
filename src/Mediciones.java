import java.util.*;

public final class Mediciones 
{
    /** Almacenamiento estático común: un tiempo por método y estructura durante esta sesión. */
    public static final Map<Estado.Tipo,EnumMap<Flujo.Metodo,Long>> TIEMPOS=new EnumMap<>(Estado.Tipo.class);
    static { for(Estado.Tipo t:Estado.Tipo.values())TIEMPOS.put(t,new EnumMap<>(Flujo.Metodo.class)); }
    public interface Trabajo { Object ejecutar(); }
    public static final class Medicion {

        public final Object resultado;
        public final Long nanosegundos;
        public Medicion(Object resultado,Long nanosegundos) { this.resultado=resultado;this.nanosegundos=nanosegundos; }
    }
    public static Medicion medir(Trabajo trabajo) {
        // TODO TIEMPO: inicio con System.nanoTime(), ejecutar trabajo exactamente una vez,
        // fin con System.nanoTime(); devolver el resultado y fin-inicio en nanosegundos.
        // Esta versión permite usar ArrayList, pero no simula una medición.
        
        Objects.requireNonNull(trabajo,"trabajo no puede ser null"); // Valida que trabajo no sea null y lanza NullPointerException si lo es.
        
        long inicio=System.nanoTime(); // Captura el instante inicial con reloj monotónico de alta precisión en nanosegundos.
        
        Object resultado=trabajo.ejecutar(); // Ejecuta el trabajo una sola vez y guarda su retorno para devolverlo sin repetir la prueba.
        
        long fin=System.nanoTime(); // Captura el instante final justo después de terminar la ejecución.
        
        return new Medicion(resultado,fin-inicio); // Devuelve el objeto Medicion con el resultado obtenido y la diferencia fin - inicio en nanosegundos.
        //return new Medicion(trabajo.ejecutar(),null);
    }
    
    public static void registrar(Estado.Tipo tipo,Flujo.Metodo metodo,long nanosegundos) {
        // TODO REGISTRO: rechazar tiempos negativos y guardar en TIEMPOS.get(tipo)
        // solo si ese método todavía no tiene tiempo. No sumar ni reemplazar el primero.
        
        if(tipo==null || metodo==null || nanosegundos<0)
                return; // Rechaza parámetros nulos o tiempos negativos sin modificar nada

        EnumMap<Flujo.Metodo,Long> mapa=TIEMPOS.get(tipo); // Obtiene el submapa de tiempos correspondiente a la estructura indicada

        if(mapa!=null)
            mapa.putIfAbsent(metodo,nanosegundos); // Guarda nanosegundos solo si el método aún no tiene tiempo registrado
    }
    public static void reiniciar() { for(Map<Flujo.Metodo,Long> tiempos:TIEMPOS.values())tiempos.clear(); }
}
