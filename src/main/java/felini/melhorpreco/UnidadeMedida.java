package felini.melhorpreco;

public class UnidadeMedida {

  final static int GRAMA      = 1;
  final static int QUILOGRAMA = 2;

  final static int MILILITRO  = 3;
  final static int LITRO      = 4;

  final static int METRO      = 5;
  final static int QUILOMETRO = 6;

  final static int UNIDADE    = 7;
  final static int OUTRO      = 8;

  private int iCodigo;

  public UnidadeMedida (Long oCodigo) {
    this.iCodigo = oCodigo.intValue();
  }
}