public class Sailor {
    private int num_afiliacion;
    private String name;
    private float peso;
    private int[] habilities;
    private float ratio_victories;
    private int id;

    public Sailor(int num_afiliacion, String name, float peso, int[] habilities, float ratio_victories) {
        this.num_afiliacion = num_afiliacion;
        this.name = name;
        this.peso = peso;
        this.habilities = habilities;
        this.ratio_victories = ratio_victories;
    }

    public int getNum_afiliacion() {
        return num_afiliacion;
    }

    public void setNum_afiliacion(int num_afiliacion) {
        this.num_afiliacion = num_afiliacion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPeso() {
        return peso;
    }
    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int[] getHabilities() {
        return habilities;
    }

    public void setHabilities(int[] habilities) {
        this.habilities = habilities;
    }

    public float getRatio_victories() {
        return ratio_victories;
    }

    public void setRatio_victories(float ratio_victories) {
        this.ratio_victories = ratio_victories;
    }
}
