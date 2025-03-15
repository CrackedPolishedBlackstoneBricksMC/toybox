package agency.highlysuspect.toybox.mapping.mappingio;

import agency.highlysuspect.toybox.mapping.Repackager;
import net.fabricmc.mappingio.MappedElementKind;
import net.fabricmc.mappingio.MappingVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Applies a Repackager to classes on their way into a Mapping-IO visitor.
 */
public class RepackagingMappingVisitor extends DelegatingMappingVisitor {
	public RepackagingMappingVisitor(@NotNull MappingVisitor delegate, Repackager repackager) {
		super(delegate);
		this.repackager = repackager;
	}
	
	protected final Repackager repackager;
	
	protected @Nullable String mapDesc(@Nullable String srcDesc) {
		if(srcDesc == null) return null;
		else return MappingUtil2.mapDesc(srcDesc, 0, srcDesc.length(), repackager::repackageOrNull);
	}
	
	@Override
	public boolean visitClass(String srcName) throws IOException {
		return delegate.visitClass(repackager.repackage(srcName));
	}
	
	@Override
	public boolean visitField(String srcName, @Nullable String srcDesc) throws IOException {
		return delegate.visitField(srcName, mapDesc(srcDesc));
	}
	
	@Override
	public boolean visitMethod(String srcName, @Nullable String srcDesc) throws IOException {
		return delegate.visitMethod(srcName, mapDesc(srcDesc));
	}
	
	@Override
	public void visitDstDesc(MappedElementKind targetKind, int namespace, String desc) throws IOException {
		delegate.visitDstDesc(targetKind, namespace, mapDesc(desc));
	}
}
