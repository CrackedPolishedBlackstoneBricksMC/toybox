package agency.highlysuspect.toybox.mapping.mappingio;

import net.fabricmc.mappingio.MappedElementKind;
import net.fabricmc.mappingio.MappingFlag;
import net.fabricmc.mappingio.MappingVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class DelegatingMappingVisitor implements MappingVisitor {
	public DelegatingMappingVisitor(@NotNull MappingVisitor delegate) {
		this.delegate = delegate;
	}
	
	protected final MappingVisitor delegate;
	
	@Override
	public Set<MappingFlag> getFlags() {
		return delegate.getFlags();
	}
	
	@Override
	public void reset() {
		delegate.reset();
	}
	
	@Override
	public boolean visitHeader() throws IOException {
		return delegate.visitHeader();
	}
	
	@Override
	public void visitNamespaces(String srcNamespace, List<String> dstNamespaces) throws IOException {
		delegate.visitNamespaces(srcNamespace, dstNamespaces);
	}
	
	@Override
	public void visitMetadata(String key, @Nullable String value) throws IOException {
		delegate.visitMetadata(key, value);
	}
	
	@Override
	public boolean visitContent() throws IOException {
		return delegate.visitContent();
	}
	
	@Override
	public boolean visitClass(String srcName) throws IOException {
		return delegate.visitClass(srcName);
	}
	
	@Override
	public boolean visitField(String srcName, @Nullable String srcDesc) throws IOException {
		return delegate.visitField(srcName, srcDesc);
	}
	
	@Override
	public boolean visitMethod(String srcName, @Nullable String srcDesc) throws IOException {
		return delegate.visitMethod(srcName, srcDesc);
	}
	
	@Override
	public boolean visitMethodArg(int argPosition, int lvIndex, @Nullable String srcName) throws IOException {
		return delegate.visitMethodArg(argPosition, lvIndex, srcName);
	}
	
	@Override
	public boolean visitMethodVar(int lvtRowIndex, int lvIndex, int startOpIdx, int endOpIdx, @Nullable String srcName) throws IOException {
		return delegate.visitMethodVar(lvtRowIndex, lvIndex, startOpIdx, endOpIdx, srcName);
	}
	
	@Override
	public boolean visitEnd() throws IOException {
		return delegate.visitEnd();
	}
	
	@Override
	public void visitDstName(MappedElementKind targetKind, int namespace, String name) throws IOException {
		delegate.visitDstName(targetKind, namespace, name);
	}
	
	@Override
	public void visitDstDesc(MappedElementKind targetKind, int namespace, String desc) throws IOException {
		delegate.visitDstDesc(targetKind, namespace, desc);
	}
	
	@Override
	public boolean visitElementContent(MappedElementKind targetKind) throws IOException {
		return delegate.visitElementContent(targetKind);
	}
	
	@Override
	public void visitComment(MappedElementKind targetKind, String comment) throws IOException {
		delegate.visitComment(targetKind, comment);
	}
}
