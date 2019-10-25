package br.feevale.vendabebidas;

public class Drink {
    private Long id;
    private String name;
    private Integer volume;
    private Integer isAlcoholic;
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(Integer alcoholic) {
        isAlcoholic = alcoholic;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
