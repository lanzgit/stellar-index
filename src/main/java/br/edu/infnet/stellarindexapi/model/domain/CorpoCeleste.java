package br.edu.infnet.stellarindexapi.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CorpoCeleste {

  @JsonProperty("object")
  private ObjectInfo object;

  @JsonProperty("orbit")
  private OrbitInfo orbit;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ObjectInfo {
    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("shortname")
    private String shortname;

    @JsonProperty("kind")
    private String kind;

    @JsonProperty("neo")
    private Boolean neo; // NearEarthObject

    @JsonProperty("pha")
    private Boolean pha; // PotentiallyHazardousAsteroid

    @JsonProperty("orbit_class")
    private OrbitClass orbitClass;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrbitClass {
      @JsonProperty("code")
      private String code;

      @JsonProperty("name")
      private String name;
    }
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class OrbitInfo {
    @JsonProperty("moid")
    private String moid;

    @JsonProperty("elements")
    private java.util.List<OrbitElement> elements;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrbitElement {
      @JsonProperty("name")
      private String name;

      @JsonProperty("value")
      private String value;

      @JsonProperty("title")
      private String title;

      @JsonProperty("units")
      private String units;
    }
  }

  public String getNome() {
    return object != null ? object.getShortname() : null;
  }

  public String getNomeCompleto() {
    return object != null ? object.getFullname() : null;
  }

  public boolean ehNEO() {
    return object != null && Boolean.TRUE.equals(object.getNeo());
  }

  public boolean ehPHA() {
    return object != null && Boolean.TRUE.equals(object.getPha());
  }

  public String getTipo() {
    return object != null ? object.getKind() : null;
  }

  public String getClassificacaoOrbital() {
    return object != null && object.getOrbitClass() != null
        ? object.getOrbitClass().getName()
        : null;
  }

  public String getCodigoClassificacao() {
    return object != null && object.getOrbitClass() != null
        ? object.getOrbitClass().getCode()
        : null;
  }

  public String getDistanciaMinima() {
    return orbit != null ? orbit.getMoid() : null;
  }

  public String getPeriodoOrbital() {
    if (orbit != null && orbit.getElements() != null) {
      return orbit.getElements().stream()
          .filter(element -> "per".equals(element.getName()))
          .map(OrbitInfo.OrbitElement::getValue)
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  public String getSemiEixoMaior() {
    if (orbit != null && orbit.getElements() != null) {
      return orbit.getElements().stream()
          .filter(element -> "a".equals(element.getName()))
          .map(OrbitInfo.OrbitElement::getValue)
          .findFirst()
          .orElse(null);
    }
    return null;
  }
}
