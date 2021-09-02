package es.ucm.gdv.offtheline;

public interface IUpdatable {

    /*
     * Metodo principal en el que se actualiza el estado del objeto que lo implemente
     *
     * @param deltaTime "Tiempo en segundos desde la ultima vez que se llamó al metodo"
     */
    void preUpdate(double deltaTime);
    void update(double deltaTime);
    void postUpdate(double deltaTime);

}
