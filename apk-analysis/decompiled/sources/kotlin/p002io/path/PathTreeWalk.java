package kotlin.p002io.path;

import java.nio.file.FileSystemLoopException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;

/* compiled from: PathTreeWalk.kt */
@Metadata(m145d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0002J\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0002J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0096\u0002JB\u0010\u0018\u001a\u00020\u0019*\b\u0012\u0004\u0012\u00020\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0018\u0010\u001f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0!\u0012\u0004\u0012\u00020\u00190 H\u0082H¢\u0006\u0002\u0010\"R\u0014\u0010\b\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0014\u0010\u000e\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000bR\u001a\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00058BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0018\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, m146d2 = {"Lkotlin/io/path/PathTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/nio/file/Path;", "start", "options", "", "Lkotlin/io/path/PathWalkOption;", "(Ljava/nio/file/Path;[Lkotlin/io/path/PathWalkOption;)V", "followLinks", "", "getFollowLinks", "()Z", "includeDirectories", "getIncludeDirectories", "isBFS", "linkOptions", "Ljava/nio/file/LinkOption;", "getLinkOptions", "()[Ljava/nio/file/LinkOption;", "[Lkotlin/io/path/PathWalkOption;", "bfsIterator", "", "dfsIterator", "iterator", "yieldIfNeeded", "", "Lkotlin/sequences/SequenceScope;", "node", "Lkotlin/io/path/PathNode;", "entriesReader", "Lkotlin/io/path/DirectoryEntriesReader;", "entriesAction", "Lkotlin/Function1;", "", "(Lkotlin/sequences/SequenceScope;Lkotlin/io/path/PathNode;Lkotlin/io/path/DirectoryEntriesReader;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib-jdk7"}, m147k = 1, m148mv = {1, 9, 0}, m150xi = 48)
/* loaded from: classes.dex */
public final class PathTreeWalk implements Sequence<Path> {
    private final PathWalkOption[] options;
    private final Path start;

    public PathTreeWalk(Path start, PathWalkOption[] options) {
        Intrinsics.checkNotNullParameter(start, "start");
        Intrinsics.checkNotNullParameter(options, "options");
        this.start = start;
        this.options = options;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getFollowLinks() {
        return ArraysKt.contains(this.options, PathWalkOption.FOLLOW_LINKS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final LinkOption[] getLinkOptions() {
        return LinkFollowing.INSTANCE.toLinkOptions(getFollowLinks());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getIncludeDirectories() {
        return ArraysKt.contains(this.options, PathWalkOption.INCLUDE_DIRECTORIES);
    }

    private final boolean isBFS() {
        return ArraysKt.contains(this.options, PathWalkOption.BREADTH_FIRST);
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<Path> iterator() {
        return isBFS() ? bfsIterator() : dfsIterator();
    }

    private final Object yieldIfNeeded(SequenceScope<? super Path> sequenceScope, PathNode node, DirectoryEntriesReader entriesReader, Function1<? super List<PathNode>, Unit> function1, Continuation<? super Unit> continuation) throws FileSystemLoopException {
        Path path = node.getPath();
        LinkOption[] linkOptions = getLinkOptions();
        LinkOption[] linkOptionArr = (LinkOption[]) Arrays.copyOf(linkOptions, linkOptions.length);
        if (Files.isDirectory(path, (LinkOption[]) Arrays.copyOf(linkOptionArr, linkOptionArr.length))) {
            if (!PathTreeWalkKt.createsCycle(node)) {
                if (getIncludeDirectories()) {
                    InlineMarker.mark(0);
                    sequenceScope.yield(path, continuation);
                    InlineMarker.mark(1);
                }
                LinkOption[] linkOptions2 = getLinkOptions();
                LinkOption[] linkOptionArr2 = (LinkOption[]) Arrays.copyOf(linkOptions2, linkOptions2.length);
                if (Files.isDirectory(path, (LinkOption[]) Arrays.copyOf(linkOptionArr2, linkOptionArr2.length))) {
                    function1.invoke(entriesReader.readEntries(node));
                }
            } else {
                throw new FileSystemLoopException(path.toString());
            }
        } else if (Files.exists(path, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1))) {
            InlineMarker.mark(0);
            sequenceScope.yield(path, continuation);
            InlineMarker.mark(1);
            return Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    /* compiled from: PathTreeWalk.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Ljava/nio/file/Path;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlin.io.path.PathTreeWalk$dfsIterator$1", m162f = "PathTreeWalk.kt", m163i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3}, m164l = {184, 190, 199, 205}, m165m = "invokeSuspend", m166n = {"$this$iterator", "stack", "entriesReader", "startNode", "this_$iv", "path$iv", "$this$iterator", "stack", "entriesReader", "$this$iterator", "stack", "entriesReader", "pathNode", "this_$iv", "path$iv", "$this$iterator", "stack", "entriesReader"}, m167s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2"})
    /* renamed from: kotlin.io.path.PathTreeWalk$dfsIterator$1 */
    static final class C10381 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super Path>, Continuation<? super Unit>, Object> {
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;

        C10381(Continuation<? super C10381> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C10381 c10381 = PathTreeWalk.this.new C10381(continuation);
            c10381.L$0 = obj;
            return c10381;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(SequenceScope<? super Path> sequenceScope, Continuation<? super Unit> continuation) {
            return ((C10381) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x0131  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x017d  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x0209  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:49:0x0207 -> B:33:0x0173). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:50:0x0209 -> B:33:0x0173). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) throws FileSystemLoopException {
            C10381 c10381;
            SequenceScope $this$iterator;
            ArrayDeque stack;
            DirectoryEntriesReader entriesReader;
            PathNode startNode;
            PathTreeWalk this_$iv;
            Path path$iv;
            ArrayDeque stack2;
            PathTreeWalk this_$iv2;
            DirectoryEntriesReader entriesReader2;
            Path path$iv2;
            PathNode startNode2;
            PathNode startNode3;
            SequenceScope $this$iterator2;
            LinkOption[] linkOptionArr;
            PathTreeWalk this_$iv3;
            Path path$iv3;
            PathTreeWalk this_$iv4;
            PathNode pathNode;
            DirectoryEntriesReader entriesReader3;
            ArrayDeque stack3;
            SequenceScope $this$iterator3;
            Path path$iv4;
            PathTreeWalk this_$iv5;
            PathNode pathNode2;
            LinkOption[] linkOptionArr2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c10381 = this;
                    $this$iterator = (SequenceScope) c10381.L$0;
                    stack = new ArrayDeque();
                    entriesReader = new DirectoryEntriesReader(PathTreeWalk.this.getFollowLinks());
                    startNode = new PathNode(PathTreeWalk.this.start, PathTreeWalkKt.keyOf(PathTreeWalk.this.start, PathTreeWalk.this.getLinkOptions()), null);
                    this_$iv = PathTreeWalk.this;
                    path$iv = startNode.getPath();
                    LinkOption[] linkOptions = this_$iv.getLinkOptions();
                    LinkOption[] linkOptionArr3 = (LinkOption[]) Arrays.copyOf(linkOptions, linkOptions.length);
                    if (!Files.isDirectory(path$iv, (LinkOption[]) Arrays.copyOf(linkOptionArr3, linkOptionArr3.length))) {
                        if (Files.exists(path$iv, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1))) {
                            c10381.L$0 = $this$iterator;
                            c10381.L$1 = stack;
                            c10381.L$2 = entriesReader;
                            c10381.label = 2;
                            if ($this$iterator.yield(path$iv, c10381) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        while (!stack.isEmpty()) {
                        }
                        return Unit.INSTANCE;
                    }
                    if (PathTreeWalkKt.createsCycle(startNode)) {
                        throw new FileSystemLoopException(path$iv.toString());
                    }
                    if (this_$iv.getIncludeDirectories()) {
                        c10381.L$0 = $this$iterator;
                        c10381.L$1 = stack;
                        c10381.L$2 = entriesReader;
                        c10381.L$3 = startNode;
                        c10381.L$4 = this_$iv;
                        c10381.L$5 = path$iv;
                        c10381.label = 1;
                        if ($this$iterator.yield(path$iv, c10381) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        stack2 = stack;
                        this_$iv2 = this_$iv;
                        entriesReader2 = entriesReader;
                        path$iv2 = path$iv;
                        startNode2 = startNode;
                        startNode3 = null;
                        $this$iterator2 = $this$iterator;
                        path$iv = path$iv2;
                        entriesReader = entriesReader2;
                        this_$iv = this_$iv2;
                        stack = stack2;
                        startNode = startNode2;
                        $this$iterator = $this$iterator2;
                    }
                    LinkOption[] linkOptions2 = this_$iv.getLinkOptions();
                    linkOptionArr = (LinkOption[]) Arrays.copyOf(linkOptions2, linkOptions2.length);
                    if (Files.isDirectory(path$iv, (LinkOption[]) Arrays.copyOf(linkOptionArr, linkOptionArr.length))) {
                        List entries = entriesReader.readEntries(startNode);
                        startNode.setContentIterator(entries.iterator());
                        stack.addLast(startNode);
                    }
                    while (!stack.isEmpty()) {
                        PathNode topNode = (PathNode) stack.last();
                        Iterator topIterator = topNode.getContentIterator();
                        Intrinsics.checkNotNull(topIterator);
                        if (topIterator.hasNext()) {
                            pathNode2 = topIterator.next();
                            this_$iv5 = PathTreeWalk.this;
                            SequenceScope $this$yieldIfNeeded$iv = $this$iterator;
                            path$iv4 = pathNode2.getPath();
                            LinkOption[] linkOptions3 = this_$iv5.getLinkOptions();
                            LinkOption[] linkOptionArr4 = (LinkOption[]) Arrays.copyOf(linkOptions3, linkOptions3.length);
                            if (Files.isDirectory(path$iv4, (LinkOption[]) Arrays.copyOf(linkOptionArr4, linkOptionArr4.length))) {
                                if (PathTreeWalkKt.createsCycle(pathNode2)) {
                                    throw new FileSystemLoopException(path$iv4.toString());
                                }
                                if (this_$iv5.getIncludeDirectories()) {
                                    c10381.L$0 = $this$iterator;
                                    c10381.L$1 = stack;
                                    c10381.L$2 = entriesReader;
                                    c10381.L$3 = pathNode2;
                                    c10381.L$4 = this_$iv5;
                                    c10381.L$5 = path$iv4;
                                    c10381.label = 3;
                                    if ($this$yieldIfNeeded$iv.yield(path$iv4, c10381) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                    entriesReader3 = entriesReader;
                                    path$iv3 = path$iv4;
                                    $this$iterator3 = $this$iterator;
                                    pathNode = pathNode2;
                                    ArrayDeque arrayDeque = stack;
                                    this_$iv4 = this_$iv5;
                                    this_$iv3 = null;
                                    stack3 = arrayDeque;
                                    pathNode2 = pathNode;
                                    $this$iterator = $this$iterator3;
                                    path$iv4 = path$iv3;
                                    entriesReader = entriesReader3;
                                    this_$iv5 = this_$iv4;
                                    stack = stack3;
                                }
                                LinkOption[] linkOptions4 = this_$iv5.getLinkOptions();
                                linkOptionArr2 = (LinkOption[]) Arrays.copyOf(linkOptions4, linkOptions4.length);
                                if (Files.isDirectory(path$iv4, (LinkOption[]) Arrays.copyOf(linkOptionArr2, linkOptionArr2.length))) {
                                    List entries2 = entriesReader.readEntries(pathNode2);
                                    pathNode2.setContentIterator(entries2.iterator());
                                    stack.addLast(pathNode2);
                                }
                                while (!stack.isEmpty()) {
                                }
                            } else if (Files.exists(path$iv4, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1))) {
                                c10381.L$0 = $this$iterator;
                                c10381.L$1 = stack;
                                c10381.L$2 = entriesReader;
                                c10381.L$3 = null;
                                c10381.L$4 = null;
                                c10381.L$5 = null;
                                c10381.label = 4;
                                if ($this$yieldIfNeeded$iv.yield(path$iv4, c10381) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            }
                        } else {
                            stack.removeLast();
                        }
                    }
                    return Unit.INSTANCE;
                case 1:
                    c10381 = this;
                    startNode3 = null;
                    path$iv2 = (Path) c10381.L$5;
                    this_$iv2 = (PathTreeWalk) c10381.L$4;
                    startNode2 = (PathNode) c10381.L$3;
                    entriesReader2 = (DirectoryEntriesReader) c10381.L$2;
                    stack2 = (ArrayDeque) c10381.L$1;
                    $this$iterator2 = (SequenceScope) c10381.L$0;
                    ResultKt.throwOnFailure($result);
                    path$iv = path$iv2;
                    entriesReader = entriesReader2;
                    this_$iv = this_$iv2;
                    stack = stack2;
                    startNode = startNode2;
                    $this$iterator = $this$iterator2;
                    LinkOption[] linkOptions22 = this_$iv.getLinkOptions();
                    linkOptionArr = (LinkOption[]) Arrays.copyOf(linkOptions22, linkOptions22.length);
                    if (Files.isDirectory(path$iv, (LinkOption[]) Arrays.copyOf(linkOptionArr, linkOptionArr.length))) {
                    }
                    while (!stack.isEmpty()) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    c10381 = this;
                    entriesReader = (DirectoryEntriesReader) c10381.L$2;
                    stack = (ArrayDeque) c10381.L$1;
                    $this$iterator = (SequenceScope) c10381.L$0;
                    ResultKt.throwOnFailure($result);
                    while (!stack.isEmpty()) {
                    }
                    return Unit.INSTANCE;
                case 3:
                    c10381 = this;
                    this_$iv3 = null;
                    path$iv3 = (Path) c10381.L$5;
                    this_$iv4 = (PathTreeWalk) c10381.L$4;
                    pathNode = (PathNode) c10381.L$3;
                    entriesReader3 = (DirectoryEntriesReader) c10381.L$2;
                    stack3 = (ArrayDeque) c10381.L$1;
                    $this$iterator3 = (SequenceScope) c10381.L$0;
                    ResultKt.throwOnFailure($result);
                    pathNode2 = pathNode;
                    $this$iterator = $this$iterator3;
                    path$iv4 = path$iv3;
                    entriesReader = entriesReader3;
                    this_$iv5 = this_$iv4;
                    stack = stack3;
                    LinkOption[] linkOptions42 = this_$iv5.getLinkOptions();
                    linkOptionArr2 = (LinkOption[]) Arrays.copyOf(linkOptions42, linkOptions42.length);
                    if (Files.isDirectory(path$iv4, (LinkOption[]) Arrays.copyOf(linkOptionArr2, linkOptionArr2.length))) {
                    }
                    while (!stack.isEmpty()) {
                    }
                    return Unit.INSTANCE;
                case 4:
                    c10381 = this;
                    entriesReader = (DirectoryEntriesReader) c10381.L$2;
                    stack = (ArrayDeque) c10381.L$1;
                    $this$iterator = (SequenceScope) c10381.L$0;
                    ResultKt.throwOnFailure($result);
                    while (!stack.isEmpty()) {
                    }
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    private final Iterator<Path> dfsIterator() {
        return SequencesKt.iterator(new C10381(null));
    }

    /* compiled from: PathTreeWalk.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Ljava/nio/file/Path;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlin.io.path.PathTreeWalk$bfsIterator$1", m162f = "PathTreeWalk.kt", m163i = {0, 0, 0, 0, 0, 0, 1, 1, 1}, m164l = {184, 190}, m165m = "invokeSuspend", m166n = {"$this$iterator", "queue", "entriesReader", "pathNode", "this_$iv", "path$iv", "$this$iterator", "queue", "entriesReader"}, m167s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2"})
    /* renamed from: kotlin.io.path.PathTreeWalk$bfsIterator$1 */
    static final class C10371 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super Path>, Continuation<? super Unit>, Object> {
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;

        C10371(Continuation<? super C10371> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C10371 c10371 = PathTreeWalk.this.new C10371(continuation);
            c10371.L$0 = obj;
            return c10371;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(SequenceScope<? super Path> sequenceScope, Continuation<? super Unit> continuation) {
            return ((C10371) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0096  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x010c  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x010a -> B:9:0x008b). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x010c -> B:9:0x008b). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) throws FileSystemLoopException {
            C10371 c10371;
            SequenceScope $this$iterator;
            ArrayDeque queue;
            DirectoryEntriesReader entriesReader;
            PathNode pathNode;
            Path path$iv;
            PathTreeWalk this_$iv;
            PathNode pathNode2;
            DirectoryEntriesReader entriesReader2;
            ArrayDeque queue2;
            SequenceScope $this$iterator2;
            Path path$iv2;
            PathTreeWalk this_$iv2;
            PathNode pathNode3;
            LinkOption[] linkOptionArr;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c10371 = this;
                    SequenceScope $this$iterator3 = (SequenceScope) c10371.L$0;
                    ArrayDeque queue3 = new ArrayDeque();
                    DirectoryEntriesReader entriesReader3 = new DirectoryEntriesReader(PathTreeWalk.this.getFollowLinks());
                    queue3.addLast(new PathNode(PathTreeWalk.this.start, PathTreeWalkKt.keyOf(PathTreeWalk.this.start, PathTreeWalk.this.getLinkOptions()), null));
                    $this$iterator = $this$iterator3;
                    queue = queue3;
                    entriesReader = entriesReader3;
                    while (!queue.isEmpty()) {
                        pathNode3 = (PathNode) queue.removeFirst();
                        this_$iv2 = PathTreeWalk.this;
                        SequenceScope $this$yieldIfNeeded$iv = $this$iterator;
                        path$iv2 = pathNode3.getPath();
                        LinkOption[] linkOptions = this_$iv2.getLinkOptions();
                        LinkOption[] linkOptionArr2 = (LinkOption[]) Arrays.copyOf(linkOptions, linkOptions.length);
                        if (Files.isDirectory(path$iv2, (LinkOption[]) Arrays.copyOf(linkOptionArr2, linkOptionArr2.length))) {
                            if (!PathTreeWalkKt.createsCycle(pathNode3)) {
                                if (this_$iv2.getIncludeDirectories()) {
                                    c10371.L$0 = $this$iterator;
                                    c10371.L$1 = queue;
                                    c10371.L$2 = entriesReader;
                                    c10371.L$3 = pathNode3;
                                    c10371.L$4 = this_$iv2;
                                    c10371.L$5 = path$iv2;
                                    c10371.label = 1;
                                    if ($this$yieldIfNeeded$iv.yield(path$iv2, c10371) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                    entriesReader2 = entriesReader;
                                    $this$iterator2 = $this$iterator;
                                    path$iv = path$iv2;
                                    pathNode2 = pathNode3;
                                    pathNode = null;
                                    queue2 = queue;
                                    this_$iv = this_$iv2;
                                    path$iv2 = path$iv;
                                    pathNode3 = pathNode2;
                                    entriesReader = entriesReader2;
                                    $this$iterator = $this$iterator2;
                                    ArrayDeque arrayDeque = queue2;
                                    this_$iv2 = this_$iv;
                                    queue = arrayDeque;
                                }
                                LinkOption[] linkOptions2 = this_$iv2.getLinkOptions();
                                linkOptionArr = (LinkOption[]) Arrays.copyOf(linkOptions2, linkOptions2.length);
                                if (Files.isDirectory(path$iv2, (LinkOption[]) Arrays.copyOf(linkOptionArr, linkOptionArr.length))) {
                                    List entries = entriesReader.readEntries(pathNode3);
                                    queue.addAll(entries);
                                }
                                while (!queue.isEmpty()) {
                                }
                            } else {
                                throw new FileSystemLoopException(path$iv2.toString());
                            }
                        } else if (Files.exists(path$iv2, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1))) {
                            c10371.L$0 = $this$iterator;
                            c10371.L$1 = queue;
                            c10371.L$2 = entriesReader;
                            c10371.L$3 = null;
                            c10371.L$4 = null;
                            c10371.L$5 = null;
                            c10371.label = 2;
                            if ($this$yieldIfNeeded$iv.yield(path$iv2, c10371) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    }
                    return Unit.INSTANCE;
                case 1:
                    c10371 = this;
                    pathNode = null;
                    path$iv = (Path) c10371.L$5;
                    this_$iv = (PathTreeWalk) c10371.L$4;
                    pathNode2 = (PathNode) c10371.L$3;
                    entriesReader2 = (DirectoryEntriesReader) c10371.L$2;
                    queue2 = (ArrayDeque) c10371.L$1;
                    $this$iterator2 = (SequenceScope) c10371.L$0;
                    ResultKt.throwOnFailure($result);
                    path$iv2 = path$iv;
                    pathNode3 = pathNode2;
                    entriesReader = entriesReader2;
                    $this$iterator = $this$iterator2;
                    ArrayDeque arrayDeque2 = queue2;
                    this_$iv2 = this_$iv;
                    queue = arrayDeque2;
                    LinkOption[] linkOptions22 = this_$iv2.getLinkOptions();
                    linkOptionArr = (LinkOption[]) Arrays.copyOf(linkOptions22, linkOptions22.length);
                    if (Files.isDirectory(path$iv2, (LinkOption[]) Arrays.copyOf(linkOptionArr, linkOptionArr.length))) {
                    }
                    while (!queue.isEmpty()) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    c10371 = this;
                    entriesReader = (DirectoryEntriesReader) c10371.L$2;
                    queue = (ArrayDeque) c10371.L$1;
                    $this$iterator = (SequenceScope) c10371.L$0;
                    ResultKt.throwOnFailure($result);
                    while (!queue.isEmpty()) {
                    }
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    private final Iterator<Path> bfsIterator() {
        return SequencesKt.iterator(new C10371(null));
    }
}
