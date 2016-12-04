package felini.melhorpreco;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

  EditText oEditPrimeiroProduto;
  EditText oEditValorPrimeiroProduto;
  EditText oEditSegundoProduto;
  EditText oEditValorSegundoProduto;

  Spinner oSpnPrimeiroProduto;
  Spinner oSpnSegundoProduto;


  @Override
  protected void onCreate (Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    oEditPrimeiroProduto      = (EditText)findViewById(R.id.editPrimeiroProduto);
    oEditValorPrimeiroProduto = (EditText)findViewById(R.id.editValorPrimeiroProduto);
    oEditSegundoProduto       = (EditText)findViewById(R.id.editSegundoProduto);
    oEditValorSegundoProduto  = (EditText)findViewById(R.id.editValorSegundoProduto);

    oSpnPrimeiroProduto = (Spinner)findViewById(R.id.spnPrimeiroProduto);
    oSpnSegundoProduto  = (Spinner)findViewById(R.id.spnSegundoProduto);
    this.configurarSpinner(oSpnPrimeiroProduto);
    this.configurarListenerSpinners();
  }


  /**
   * @return Spinner
   */
  public Spinner getSegundoSpinner () {
    return this.oSpnSegundoProduto;
  }

  /**
   * Limpa o spinner deixando apenas a opção Selecione
   * @param oSpinner
   */
  public void limparSpinner(Spinner oSpinner) {

    ArrayAdapter<CharSequence> oArrayAdapter;
    oArrayAdapter =
      ArrayAdapter.createFromResource(
        MainActivity.this,
        R.array.unidades_medida_selecione,
        android.R.layout.simple_spinner_item);
    oArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    oSpinner.setAdapter(oArrayAdapter);
  }

  /**
   * Configura o spinner com as opções de acordo com a seleção do primeiro spinner.
   * Dessa forma o usuário sempre irá comparar medidas correspondentes
   */
  protected void configuraSegundoSpinner() {

    ArrayAdapter<CharSequence> oArrayAdapter;
    Long oValorPrimeiroProduto = oSpnPrimeiroProduto.getSelectedItemId();
    switch (oValorPrimeiroProduto.intValue()) {

      case UnidadeMedida.GRAMA:
      case UnidadeMedida.QUILOGRAMA:
        oArrayAdapter =
          ArrayAdapter.createFromResource(
            MainActivity.this,
            R.array.unidades_medida_peso,
            android.R.layout.simple_spinner_item
          );
        break;

      case UnidadeMedida.LITRO:
      case UnidadeMedida.MILILITRO:
        oArrayAdapter =
          ArrayAdapter.createFromResource(
            MainActivity.this,
            R.array.unidades_medida_liquido,
            android.R.layout.simple_spinner_item
          );
        break;

      case UnidadeMedida.METRO:
      case UnidadeMedida.QUILOMETRO:
        oArrayAdapter =
          ArrayAdapter.createFromResource(
            MainActivity.this,
            R.array.unidades_medida_distancia,
            android.R.layout.simple_spinner_item
          );
        break;

      case UnidadeMedida.UNIDADE:
        oArrayAdapter =
          ArrayAdapter.createFromResource(
            MainActivity.this,
            R.array.unidades_medida_unidade,
            android.R.layout.simple_spinner_item
          );
        break;

      case UnidadeMedida.OUTRO:
        oArrayAdapter =
          ArrayAdapter.createFromResource(
            MainActivity.this,
            R.array.unidades_medida_outro,
            android.R.layout.simple_spinner_item
          );
        break;

      default:
        oArrayAdapter =
          ArrayAdapter.createFromResource(
            MainActivity.this,
            R.array.unidades_medida_selecione,
            android.R.layout.simple_spinner_item
          );
    }

    oArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    oSpnSegundoProduto.setAdapter(oArrayAdapter);
  }

  /**
   * Método que cria as opções para o Spinner Passado por Parâmetro
   * @param oSpinner
   */
  protected void configurarSpinner(Spinner oSpinner) {

    ArrayAdapter<CharSequence> oArrayAdapter =
      ArrayAdapter.createFromResource(
        this,
        R.array.unidades_medida,
        android.R.layout.simple_spinner_item);
    oArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    oSpinner.setAdapter(oArrayAdapter);
  }

  /**
   * Descobre qual o produto mais barato
   * @param oView
   */
  public void getProdutoMaisBarato(View oView) {

    String sMensagemRetorno = "";
    try {

      this.validar();
      sMensagemRetorno = this.calcular();

    } catch (Exception oException) {
      sMensagemRetorno = oException.getMessage();
    }
    Toast.makeText(this, sMensagemRetorno, Toast.LENGTH_LONG).show();
  }

  /**
   * Limpa os campos da tela
   * @param oView
   */
  public void limparCampos(View oView) {

    oEditPrimeiroProduto.setText("");
    oEditValorPrimeiroProduto.setText("");
    oEditSegundoProduto.setText("");
    oEditValorSegundoProduto.setText("");
    limparSpinner(oSpnSegundoProduto);
    configurarSpinner(oSpnPrimeiroProduto);
  }

  private void configurarListenerSpinners() {


    AdapterView.OnItemSelectedListener oListenerPrimeiroProduto = new AdapterView.OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
          MainActivity.this.limparSpinner(getSegundoSpinner());
        } else {
          MainActivity.this.configuraSegundoSpinner();
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    };
    oSpnPrimeiroProduto.setOnItemSelectedListener(oListenerPrimeiroProduto);
  }

  /**
   * Valida as informações passadas na tela antes de processar com o cálculo
   * @throws Exception
   */
  private void validar() throws Exception {

    String sPrimeiroProduto      = oEditPrimeiroProduto.getText().toString().trim();
    String sValorPrimeiroProduto = oEditValorPrimeiroProduto.getText().toString().trim();
    String sSegundoProduto       = oEditSegundoProduto.getText().toString().trim();
    String sValorSegundoProduto  = oEditValorSegundoProduto.getText().toString().trim();

    if (sPrimeiroProduto.length() == 0) {
      throw new Exception(getString(R.string.erro_quantidade_primeiro_produto));
    }

    if (sValorPrimeiroProduto.length() == 0) {
      throw new Exception(getString(R.string.erro_valor_primeiro_produto));
    }

    if (sSegundoProduto.length() == 0) {
      throw new Exception(getString(R.string.erro_quantidade_segundo_produto));
    }

    if (sValorSegundoProduto.length() == 0) {
      throw new Exception(getString(R.string.erro_valor_segundo_produto));
    }

    if (oSpnPrimeiroProduto.getSelectedItemId() == 0) {
      throw new Exception(getString(R.string.erro_unidade_medida));
    }
  }

  /**
   * Verifica qual o produto possui o valor mais barato
   * @return String
   */
  private String calcular() {

    float nQuantidadePrimeiroProduto = Float.parseFloat(oEditPrimeiroProduto.getText().toString());
    float nValorPrimeiroProduto      = Float.parseFloat(oEditValorPrimeiroProduto.getText().toString());

    Long oPosicaoPrimeiroProduto = oSpnPrimeiroProduto.getSelectedItemId();
    switch (oPosicaoPrimeiroProduto.intValue()) {

      case UnidadeMedida.QUILOGRAMA:
      case UnidadeMedida.LITRO:
      case UnidadeMedida.QUILOMETRO:

        nQuantidadePrimeiroProduto = (nQuantidadePrimeiroProduto*1000);

        break;
    }

    float nQuantidadeSegundoProduto = Float.parseFloat(oEditSegundoProduto.getText().toString());
    float nValorSegundoProduto      = Float.parseFloat(oEditValorSegundoProduto.getText().toString());

    /**
     * Verifica se é necessário multiplicar a quantidade do segundo item por 1000.
     */
    if (oSpnSegundoProduto.getSelectedItemId() > 0) {
      nQuantidadeSegundoProduto = (nQuantidadeSegundoProduto*1000);
    }

    /**
     * Processa os valores calculados
     */
    float nCalculoProduto1 = (nValorPrimeiroProduto/nQuantidadePrimeiroProduto);
    float nCalculoProduto2 = (nValorSegundoProduto/nQuantidadeSegundoProduto);

    String sRetorno = getString(R.string.msg_produto_iguais);
    if (nCalculoProduto1 > nCalculoProduto2) {

      float nMultiplicaSegundoProduto = (nCalculoProduto2 * nQuantidadePrimeiroProduto);
      String sProduto = getString(R.string.msg_produto_segundo);
      if (nMultiplicaSegundoProduto >= nValorPrimeiroProduto) {
        sProduto = getString(R.string.msg_produto_primeiro);
      }

      Float nPorcentagem = this.getPorcentagemProduto(nValorPrimeiroProduto, nMultiplicaSegundoProduto);

      sRetorno = String.format(getString(R.string.msg_produto_barato), sProduto, nPorcentagem);

    } else if (nCalculoProduto2 > nCalculoProduto1) {

      String sProduto = getString(R.string.msg_produto_primeiro);
      float nMultiplicaPrimeiroProduto = (nCalculoProduto1 * nQuantidadeSegundoProduto);
      if (nMultiplicaPrimeiroProduto >= nValorSegundoProduto) {
        sProduto = getString(R.string.msg_produto_segundo);
      }
      Float nPorcentagem = this.getPorcentagemProduto(nValorSegundoProduto, nMultiplicaPrimeiroProduto);
      sRetorno = String.format(getString(R.string.msg_produto_barato), sProduto, nPorcentagem);
    }
    return sRetorno;
  }

  /**
   * @param nTotalPrimeiroProduto
   * @param nTotalSegundoProduto
   * @return float
   */
  public static Float getPorcentagemProduto(float nTotalPrimeiroProduto, float nTotalSegundoProduto) {

    Float nPorcentagem = new Float(0);
    if (nTotalPrimeiroProduto > nTotalSegundoProduto) {

      nTotalPrimeiroProduto = (nTotalSegundoProduto * 100) / nTotalPrimeiroProduto;
      nPorcentagem = (100 - nTotalPrimeiroProduto);

    } else if (nTotalPrimeiroProduto < nTotalSegundoProduto) {

      nTotalSegundoProduto = (nTotalPrimeiroProduto * 100) / nTotalSegundoProduto;
      nPorcentagem = (100 - nTotalSegundoProduto);
    }
    return nPorcentagem < 0 ? nPorcentagem * -1 : nPorcentagem;
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {

    switch (item.getItemId()) {

      case R.id.action_sair:

        finish();
        break;

      case R.id.action_sobre:

        Intent oIntentAbout = new Intent(this, Sobre.class);
        startActivity(oIntentAbout);
        break;

      default:
        Toast.makeText(this, getString(R.string.opcao_nao_encontrada), Toast.LENGTH_SHORT).show();
    }
    return super.onOptionsItemSelected(item);
  }
}
