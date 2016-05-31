package SCATTLABS.ZKUpload.zkoss;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;

public class TestVM2 {

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("ayut") String param) {
		System.out.println("param : " + param);
		// Your Business logic
	}

	@Command
	public void sendData() {
		Map<String, Object> args = new HashMap<>();
		args.put("myData", "please check");
		System.out.println("sendData");
		BindUtils.postGlobalCommand(null, null, "recieveData", args);
	}
}
