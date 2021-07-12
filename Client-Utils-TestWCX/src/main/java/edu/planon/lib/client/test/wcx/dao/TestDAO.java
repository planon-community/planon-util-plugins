package edu.planon.lib.client.test.wcx.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.planon.lib.client.common.dto.PnFieldDTO;
import edu.planon.lib.client.common.dto.PnRecordDTO;
import edu.planon.lib.client.common.dto.PnReferenceFieldDTO;
import edu.planon.lib.client.common.exception.PnClientException;
import edu.planon.lib.client.test.wcx.exception.WCXException;
import edu.planon.lib.client.test.wcx.settings.SettingsTestWCX;
import edu.planon.lib.common.exception.PropertyNotDefined;
import nl.planon.enterprise.service.api.PnESActionNotFoundException;
import nl.planon.enterprise.service.api.PnESBusinessException;
import nl.planon.enterprise.service.api.PnESFieldNotFoundException;
import nl.planon.enterprise.service.api.PnESValueType;
import nl.planon.zeus.clientextension.cxinterface.ICXBusinessObject;
import nl.planon.zeus.clientextension.cxinterface.ICXContext;

public class TestDAO implements Serializable {
	private static final long serialVersionUID = 8428292668691492041L;
	protected final SettingsTestWCX settings;
	protected final ICXContext cxContext;
	
	protected List<PnFieldDTO<?>> fieldList = new ArrayList<PnFieldDTO<?>>();
	
	public TestDAO(SettingsTestWCX settings, ICXContext cxContext) throws PnClientException {
		this.settings = settings;
		this.cxContext = cxContext;
		ICXBusinessObject currentCXBO = cxContext.getCurrentBO();
		
		try {
			String boPnName = settings.getReferenceFieldBOPnName();
			PnReferenceFieldDTO refFieldDefDTO = new PnReferenceFieldDTO("RefPnName", settings.getReferenceFieldLabel(), boPnName,
					settings.getReferenceFieldDisplayFields());
			refFieldDefDTO.setRequired(true);
			if (!Objects.isNull(currentCXBO) && currentCXBO.getBOPnName().equals(boPnName)) {
				Integer primaryKey = currentCXBO.getFieldByName("Syscode").getValueAsInteger();
				refFieldDefDTO.setValue(new PnRecordDTO(primaryKey, currentCXBO.getFieldByName("Code").getValueAsString()));
			}
			this.fieldList.add(refFieldDefDTO);
		}
		catch (PropertyNotDefined e) {
			
		}
		
		this.fieldList.add(PnFieldDTO.create("IntegerPnName", "Integer", PnESValueType.INTEGER));
		this.fieldList.add(PnFieldDTO.create("BigDecimalPnName", "BigDecimal", PnESValueType.BIG_DECIMAL));
		this.fieldList.add(PnFieldDTO.create("DatePnName", "Date", PnESValueType.DATE_NEUTRAL));
		this.fieldList.add(PnFieldDTO.create("DateTimePnName", "DateTime", PnESValueType.DATE_TIME));
		this.fieldList.add(PnFieldDTO.create("TimePnName", "Time", PnESValueType.TIME_NEUTRAL));
		this.fieldList.add(PnFieldDTO.create("BooleanPnName", "Boolean", PnESValueType.BOOLEAN));

		PnFieldDTO<String> textAreaField = new PnFieldDTO<String>(String.class, "TextAreaPnName", "TextArea", PnESValueType.STRING);
		textAreaField.setInputLength(500);
		this.fieldList.add(textAreaField);
	}
	
	public List<PnFieldDTO<?>> getFields() {
		return this.fieldList;
	}
	
	public ArrayList<String> reportValues() throws WCXException, PnESBusinessException, PnESActionNotFoundException, PnESFieldNotFoundException {
		ArrayList<String> resultMessages = new ArrayList<String>();
		resultMessages.add("The following values were selected:");
		
		for (PnFieldDTO<?> field : this.fieldList) {
			String label = field.getLabel();
			String fieldPnName = field.getPnName();
			String dataType = field.getDataType().getSimpleName();
			String fieldType = field.getFieldType().toString();
			String value = (field.getValue() == null) ? "null" : field.getValue().toString();
			
			resultMessages.add("");
			resultMessages.add(label + " (" + fieldPnName + ")");
			resultMessages.add("Data Type: " + dataType);
			resultMessages.add("Field Type: " + fieldType);
			resultMessages.add("Value: " + value);
		}
		return resultMessages;
	}
	
}
