package net.kernal.spiderman.worker.extract;

import java.util.Collection;

import net.kernal.spiderman.K;
import net.kernal.spiderman.Properties;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.Extractor.Callback.FieldEntry;
import net.kernal.spiderman.worker.extract.Extractor.Callback.ModelEntry;
import net.kernal.spiderman.worker.extract.conf.Field;
import net.kernal.spiderman.worker.extract.conf.Model;

public class LinksExtractor extends Extractor {

	public LinksExtractor(ExtractTask task, String page, Model... models) {
		super(task, page, models);
	}

	public void extract(Callback callback) {
		final Downloader.Response response = getTask().getResponse();
		final Collection<?> urls = URLKit.links(response);
		if (K.isNotEmpty(urls)) {
			Field field = new Field(getPage(), null, "urls");
			field.set("isForNewTask", true);
			field.set("isArray", true);
			field.set("isDistinct", true);
			final FieldEntry fieldEntry = new FieldEntry(field, urls);
			callback.onFieldExtracted(fieldEntry);
			
			final Model model = new Model(getPage(), "");
			final Properties fields = new Properties();
			fields.put("links", fieldEntry.getData());
			fields.put("url", response.getRequest().getUrl());
			final ModelEntry modelEntry = new ModelEntry(model, fields);
			callback.onModelExtracted(modelEntry);
		}
	}

}
