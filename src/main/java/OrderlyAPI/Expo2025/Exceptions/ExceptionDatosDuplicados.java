package OrderlyAPI.Expo2025.Exceptions;

import lombok.Getter;

public class ExceptionDatosDuplicados extends RuntimeException {

  @Getter
  private String campoDuplicado;

  public ExceptionDatosDuplicados(String message, String campoDuplicado) {
    super(message);
    this.campoDuplicado = campoDuplicado;
  }

  public ExceptionDatosDuplicados(String message) {
        super(message);
    }
}
