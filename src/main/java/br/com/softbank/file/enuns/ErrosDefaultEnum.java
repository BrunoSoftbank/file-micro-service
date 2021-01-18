package br.com.softbank.file.enuns;

public enum ErrosDefaultEnum {

	TIPO_MIDIA_NAO_SUPORTADO("Apenas arquivos com extensão xlsx são suportados para este recurso");
	
	private String descricao;
	
	ErrosDefaultEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
