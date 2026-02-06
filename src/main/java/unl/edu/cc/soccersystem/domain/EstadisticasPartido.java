package unl.edu.cc.soccersystem.domain;

public class EstadisticasPartido {

    private String marcadorFinal;
    private int tirosAlArco;
    private int faltasCometidas;
    private int tarjetasAmarillas;
    private int tarjetasRojas;
    private int corners;
    private int totalDeCambios;
    private Sancion sancion;

    public EstadisticasPartido(String marcadorFinal) {
        this.marcadorFinal = marcadorFinal;
    }

    public EstadisticasPartido(String marcadorFinal, int totalDeCambios,
                              int corners, int tarjetasAmarillas, int tarjetasRojas, int faltasCometidas,
                              int tirosAlArco, Sancion sancion) {
        this.marcadorFinal = marcadorFinal;
        this.totalDeCambios = totalDeCambios;
        this.corners = corners;
        this.tarjetasAmarillas = tarjetasAmarillas;
        this.tarjetasRojas = tarjetasRojas;
        this.faltasCometidas = faltasCometidas;
        this.tirosAlArco = tirosAlArco;
        this.sancion = sancion;
    }


    public String getMarcadorFinal() {
        return marcadorFinal;
    }

    public void setMarcadorFinal(String marcadorFinal) {
        this.marcadorFinal = marcadorFinal;
    }

    public int getTirosAlArco() {
        return tirosAlArco;
    }

    public void setTirosAlArco(int tirosAlArco) {
        if(tirosAlArco < 0){
            throw new IllegalArgumentException("Los Tiros al Arco no pueden ser negativos");
        }
        this.tirosAlArco = tirosAlArco;
    }

    public int getFaltasCometidas() {
        return faltasCometidas;
    }

    public void setFaltasCometidas(int faltasCometidas) {
        this.faltasCometidas = faltasCometidas;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public void setTarjetasAmarillas(int tarjetasAmarillas) {
        this.tarjetasAmarillas = tarjetasAmarillas;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas;
    }

    public void setTarjetasRojas(int tarjetasRojas) {
        this.tarjetasRojas = tarjetasRojas;
    }

    public int getCorners() {
        return corners;
    }

    public void setCorners(int corners) {
        this.corners = corners;
    }

    public int getTotalDeCambios() {
        return totalDeCambios;
    }

    public void setTotalDeCambios(int totalDeCambios) {
        this.totalDeCambios = totalDeCambios;
    }

    @Override
    public String toString() {
        return "EstadisticaPartido{" +
                "marcadorFinal='" + marcadorFinal + '\'' +
                ", tirosAlArco=" + tirosAlArco +
                ", faltasCometidas=" + faltasCometidas +
                ", tarjetasAmarillas=" + tarjetasAmarillas +
                ", tarjetasRojas=" + tarjetasRojas +
                ", corners=" + corners +
                ", totalDeCambios=" + totalDeCambios +
                '}';
    }
}
