package SCATTLABS.ZKUpload.zkoss;

import java.util.HashMap;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

public class TestVM1 {
	@Wire("#win")
	private Window win;

	@Init
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	public void openWindow() {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("ayut", "java");
		Executions.createComponents("pm.zul", win, hashMap);
	}

	@GlobalCommand
	public void recieveData(@BindingParam("myData") String myData) {
		System.out.println(myData);
	}
}