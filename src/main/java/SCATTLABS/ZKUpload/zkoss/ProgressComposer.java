package SCATTLABS.ZKUpload.zkoss;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Window;

import SCATTLABS.ZKUpload.model.Upload;
import SCATTLABS.ZKUpload.service.CRUDService;

public class ProgressComposer extends SelectorComposer<Window> {

	private static final long serialVersionUID = -8327879927085566401L;

	@Wire
	private Progressmeter pm;
	@Wire
	private Label lbl;
	@WireVariable
	private Desktop desktop;

	@WireVariable
	private CRUDService CRUDService;

	private int all = 10;

	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("ayut") String param) {
		System.out.println("param : " + param);
		startProgress();
		// Your Business logic
	}

	public ProgressComposer() {
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		List<Upload> uploads = CRUDService.getAll(Upload.class);
		System.out.println("size : " + uploads.size());
		for (Upload upload : uploads) {
			System.out.println("fileName : " + upload.getFile_name());
		}
		// TODO Auto-generated constructor stub
	}

	private Thread myThread = new Thread() {
		public void run() {
			System.out.println("desktop : " + desktop.isServerPushEnabled());
			if (!desktop.isServerPushEnabled())
				return;

			try {
				for (int i = 1; i <= all; i++) {
					long ms = (long) (Math.random() * 2000); // simulate long
					System.out.println("ms : " + ms); // operation
					Thread.sleep(ms);

					Executions.activate(desktop);
					progress(i);
					Executions.deactivate(desktop);
				}
				Executions.activate(desktop);
				progress(0);
				Executions.deactivate(desktop);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	@Listen("onClick = #btn")
	public void startProgress() {
		desktop.enableServerPush(true);
		myThread.start();
		System.out.println("tread lewat");
	}

	@Command
	public void start() {
		desktop.enableServerPush(true);
		myThread.start();
		System.out.println("tread lewat");
	}

	private void progress(int current) {
		if (current == 0) {
			pm.setValue(current);
			lbl.setValue("" + current);
		} else {
			System.out.println("progress : " + current);
			int value = current * 100 / all;
			pm.setValue(value);
			lbl.setValue("" + value);
		}

		if (current == 0) {
			desktop.enableServerPush(false);
		}
	}
}