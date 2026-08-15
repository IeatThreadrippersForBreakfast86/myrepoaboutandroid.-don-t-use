package kotlinx.coroutines.channels;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.android.material.card.MaterialCardViewHelper;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.IndexedValue;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.channels.ReceiveChannel;
import org.xbill.DNS.WKSRecord;

/* compiled from: Deprecated.kt */
@Metadata(m145d1 = {"\u0000®\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u001f\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001aJ\u0010\u0000\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\u0005\u0012\u0004\u0012\u00020\u00060\u0001j\u0002`\u00072\u001a\u0010\b\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\n0\t\"\u0006\u0012\u0002\b\u00030\nH\u0001¢\u0006\u0002\u0010\u000b\u001a\u001e\u0010\f\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001aC\u0010\u0010\u001a\u0002H\u0011\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u000e0\u00122\u001d\u0010\u0013\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\n\u0012\u0004\u0012\u0002H\u00110\u0001¢\u0006\u0002\b\u0014H\u0087\b¢\u0006\u0002\u0010\u0015\u001a2\u0010\u0016\u001a\u00020\u0006\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u00122\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u00020\u00060\u0001H\u0087H¢\u0006\u0002\u0010\u0018\u001a1\u0010\u0019\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\u0005\u0012\u0004\u0012\u00020\u00060\u0001j\u0002`\u0007*\u0006\u0012\u0002\b\u00030\nH\u0001\u001a\u001e\u0010\u001a\u001a\u00020\u001b\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0007\u001aW\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u001e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2\"\u0010!\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u001e0#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0001¢\u0006\u0002\u0010%\u001a0\u0010&\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010'\u001a\u00020\u001b2\b\b\u0002\u0010\u001f\u001a\u00020 H\u0007\u001aQ\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2\"\u0010)\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0007¢\u0006\u0002\u0010%\u001a&\u0010*\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010+\u001a\u00020\u001bH\u0087@¢\u0006\u0002\u0010,\u001a(\u0010-\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010+\u001a\u00020\u001bH\u0087@¢\u0006\u0002\u0010,\u001aQ\u0010.\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2\"\u0010)\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0001¢\u0006\u0002\u0010%\u001af\u0010/\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 27\u0010)\u001a3\b\u0001\u0012\u0013\u0012\u00110\u001b¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(+\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0#\u0012\u0006\u0012\u0004\u0018\u00010$00H\u0007¢\u0006\u0002\u00101\u001aQ\u00102\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2\"\u0010)\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0007¢\u0006\u0002\u0010%\u001a$\u00103\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\b\b\u0000\u0010\u000e*\u00020$*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\nH\u0001\u001a>\u00104\u001a\u0002H5\"\b\b\u0000\u0010\u000e*\u00020$\"\u0010\b\u0001\u00105*\n\u0012\u0006\b\u0000\u0012\u0002H\u000e06*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\n2\u0006\u00107\u001a\u0002H5H\u0087@¢\u0006\u0002\u00108\u001a<\u00104\u001a\u0002H5\"\b\b\u0000\u0010\u000e*\u00020$\"\u000e\b\u0001\u00105*\b\u0012\u0004\u0012\u0002H\u000e09*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\n2\u0006\u00107\u001a\u0002H5H\u0087@¢\u0006\u0002\u0010:\u001a\u001e\u0010;\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a \u0010<\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a]\u0010=\u001a\b\u0012\u0004\u0012\u0002H\u00110\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2(\u0010>\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110\n0#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0007¢\u0006\u0002\u0010%\u001a&\u0010?\u001a\u00020\u001b\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010@\u001a\u0002H\u000eH\u0087@¢\u0006\u0002\u0010A\u001a\u001e\u0010B\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a&\u0010C\u001a\u00020\u001b\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010@\u001a\u0002H\u000eH\u0087@¢\u0006\u0002\u0010A\u001a \u0010D\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001aW\u0010E\u001a\b\u0012\u0004\u0012\u0002H\u00110\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2\"\u0010>\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0001¢\u0006\u0002\u0010%\u001al\u0010F\u001a\b\u0012\u0004\u0012\u0002H\u00110\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 27\u0010>\u001a3\b\u0001\u0012\u0013\u0012\u00110\u001b¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(+\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00110#\u0012\u0006\u0012\u0004\u0018\u00010$00H\u0001¢\u0006\u0002\u00101\u001ar\u0010G\u001a\b\u0012\u0004\u0012\u0002H\u00110\n\"\u0004\b\u0000\u0010\u000e\"\b\b\u0001\u0010\u0011*\u00020$*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 29\u0010>\u001a5\b\u0001\u0012\u0013\u0012\u00110\u001b¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(+\u0012\u0004\u0012\u0002H\u000e\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00110#\u0012\u0006\u0012\u0004\u0018\u00010$00H\u0007¢\u0006\u0002\u00101\u001a]\u0010H\u001a\b\u0012\u0004\u0012\u0002H\u00110\n\"\u0004\b\u0000\u0010\u000e\"\b\b\u0001\u0010\u0011*\u00020$*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2$\u0010>\u001a \b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00110#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0007¢\u0006\u0002\u0010%\u001a<\u0010I\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u001a\u0010J\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000e0Kj\n\u0012\u0006\b\u0000\u0012\u0002H\u000e`LH\u0087@¢\u0006\u0002\u0010M\u001a<\u0010N\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u001a\u0010J\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000e0Kj\n\u0012\u0006\b\u0000\u0012\u0002H\u000e`LH\u0087@¢\u0006\u0002\u0010M\u001a\u001e\u0010O\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a$\u0010P\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\b\b\u0000\u0010\u000e*\u00020$*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\nH\u0007\u001a\u001e\u0010Q\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a \u0010R\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a0\u0010S\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010'\u001a\u00020\u001b2\b\b\u0002\u0010\u001f\u001a\u00020 H\u0007\u001aQ\u0010T\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 2\"\u0010)\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0#\u0012\u0006\u0012\u0004\u0018\u00010$0\"H\u0007¢\u0006\u0002\u0010%\u001a6\u0010U\u001a\u0002H5\"\u0004\b\u0000\u0010\u000e\"\u000e\b\u0001\u00105*\b\u0012\u0004\u0012\u0002H\u000e09*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u00107\u001a\u0002H5H\u0081@¢\u0006\u0002\u0010:\u001a8\u0010V\u001a\u0002H5\"\u0004\b\u0000\u0010\u000e\"\u0010\b\u0001\u00105*\n\u0012\u0006\b\u0000\u0012\u0002H\u000e06*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u00107\u001a\u0002H5H\u0081@¢\u0006\u0002\u00108\u001a<\u0010W\u001a\u000e\u0012\u0004\u0012\u0002H\u001e\u0012\u0004\u0012\u0002HY0X\"\u0004\b\u0000\u0010\u001e\"\u0004\b\u0001\u0010Y*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u001e\u0012\u0004\u0012\u0002HY0Z0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001aR\u0010W\u001a\u0002H[\"\u0004\b\u0000\u0010\u001e\"\u0004\b\u0001\u0010Y\"\u0018\b\u0002\u0010[*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u001e\u0012\u0006\b\u0000\u0012\u0002HY0\\*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u001e\u0012\u0004\u0012\u0002HY0Z0\n2\u0006\u00107\u001a\u0002H[H\u0081@¢\u0006\u0002\u0010]\u001a$\u0010^\u001a\b\u0012\u0004\u0012\u0002H\u000e0_\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a$\u0010`\u001a\b\u0012\u0004\u0012\u0002H\u000e0a\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0081@¢\u0006\u0002\u0010\u000f\u001a$\u0010b\u001a\b\u0012\u0004\u0012\u0002H\u000e0c\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@¢\u0006\u0002\u0010\u000f\u001a.\u0010d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u001f\u001a\u00020 H\u0007\u001a?\u0010f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H\u00110Z0\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u0011*\b\u0012\u0004\u0012\u0002H\u000e0\n2\f\u0010g\u001a\b\u0012\u0004\u0012\u0002H\u00110\nH\u0087\u0004\u001az\u0010f\u001a\b\u0012\u0004\u0012\u0002HY0\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u0011\"\u0004\b\u0002\u0010Y*\b\u0012\u0004\u0012\u0002H\u000e0\n2\f\u0010g\u001a\b\u0012\u0004\u0012\u0002H\u00110\n2\b\b\u0002\u0010\u001f\u001a\u00020 26\u0010>\u001a2\u0012\u0013\u0012\u0011H\u000e¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(h\u0012\u0013\u0012\u0011H\u0011¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(i\u0012\u0004\u0012\u0002HY0\"H\u0001¨\u0006j"}, m146d2 = {"consumesAll", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "Lkotlinx/coroutines/CompletionHandler;", "channels", "", "Lkotlinx/coroutines/channels/ReceiveChannel;", "([Lkotlinx/coroutines/channels/ReceiveChannel;)Lkotlin/jvm/functions/Function1;", "any", "", "E", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "consume", "R", "Lkotlinx/coroutines/channels/BroadcastChannel;", "block", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "consumeEach", "action", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "consumes", "count", "", "distinct", "distinctBy", "K", "context", "Lkotlin/coroutines/CoroutineContext;", "selector", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/channels/ReceiveChannel;", "drop", "n", "dropWhile", "predicate", "elementAt", "index", "(Lkotlinx/coroutines/channels/ReceiveChannel;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "elementAtOrNull", "filter", "filterIndexed", "Lkotlin/Function3;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/channels/ReceiveChannel;", "filterNot", "filterNotNull", "filterNotNullTo", "C", "", "destination", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Collection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/SendChannel;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "first", "firstOrNull", "flatMap", "transform", "indexOf", "element", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "last", "lastIndexOf", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "mapNotNull", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Comparator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "minWith", "none", "requireNoNulls", "single", "singleOrNull", "take", "takeWhile", "toChannel", "toCollection", "toMap", "", "V", "Lkotlin/Pair;", "M", "", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toMutableList", "", "toMutableSet", "", "toSet", "", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "kotlinx-coroutines-core"}, m147k = 5, m148mv = {1, 9, 0}, m150xi = 48, m151xs = "kotlinx/coroutines/channels/ChannelsKt")
/* loaded from: classes.dex */
final /* synthetic */ class ChannelsKt__DeprecatedKt {

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0}, m164l = {434}, m165m = "any", m166n = {"$this$consume$iv"}, m167s = {"L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$any$1 */
    static final class C11341<E> extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C11341(Continuation<? super C11341> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.any(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 176)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0}, m164l = {41}, m165m = "consumeEach", m166n = {"action", "channel$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$consumeEach$1 */
    static final class C11351<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11351(Continuation<? super C11351> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.consumeEach(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0}, m164l = {517}, m165m = "count", m166n = {"count", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$count$1 */
    static final class C11381<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11381(Continuation<? super C11381> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.count(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 0}, m164l = {WKSRecord.Service.BOOTPC}, m165m = "elementAt", m166n = {"$this$consume$iv", "index", "count"}, m167s = {"L$0", "I$0", "I$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$elementAt$1 */
    static final class C11431<E> extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C11431(Continuation<? super C11431> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.elementAt(null, 0, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 0}, m164l = {83}, m165m = "elementAtOrNull", m166n = {"$this$consume$iv", "index", "count"}, m167s = {"L$0", "I$0", "I$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$elementAtOrNull$1 */
    static final class C11441<E> extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C11441(Continuation<? super C11441> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.elementAtOrNull(null, 0, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0}, m164l = {517}, m165m = "filterNotNullTo", m166n = {"destination", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNullTo$1 */
    static final class C11491<E, C extends Collection<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11491(Continuation<? super C11491> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.filterNotNullTo((ReceiveChannel) null, (Collection) null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1}, m164l = {517, 272}, m165m = "filterNotNullTo", m166n = {"destination", "$this$consume$iv$iv", "destination", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNullTo$3 */
    static final class C11503<E, C extends SendChannel<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11503(Continuation<? super C11503> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.filterNotNullTo((ReceiveChannel) null, (SendChannel) null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0}, m164l = {WKSRecord.Service.SUPDUP}, m165m = "first", m166n = {"$this$consume$iv", "iterator"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$first$1 */
    static final class C11511<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C11511(Continuation<? super C11511> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.first(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0}, m164l = {105}, m165m = "firstOrNull", m166n = {"$this$consume$iv", "iterator"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$firstOrNull$1 */
    static final class C11521<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C11521(Continuation<? super C11521> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.firstOrNull(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 0}, m164l = {517}, m165m = "indexOf", m166n = {"element", "index", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$indexOf$1 */
    static final class C11541<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C11541(Continuation<? super C11541> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.indexOf(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1, 1}, m164l = {127, WKSRecord.Service.CISCO_FNA}, m165m = "last", m166n = {"$this$consume$iv", "iterator", "$this$consume$iv", "iterator", "last"}, m167s = {"L$0", "L$1", "L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$last$1 */
    static final class C11551<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11551(Continuation<? super C11551> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.last(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 0, 0}, m164l = {517}, m165m = "lastIndexOf", m166n = {"element", "lastIndex", "index", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1", "L$2", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$lastIndexOf$1 */
    static final class C11561<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;
        /* synthetic */ Object result;

        C11561(Continuation<? super C11561> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.lastIndexOf(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1, 1}, m164l = {153, 156}, m165m = "lastOrNull", m166n = {"$this$consume$iv", "iterator", "$this$consume$iv", "iterator", "last"}, m167s = {"L$0", "L$1", "L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$lastOrNull$1 */
    static final class C11571<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11571(Continuation<? super C11571> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.lastOrNull(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 0, 1, 1, 1, 1}, m164l = {450, 452}, m165m = "maxWith", m166n = {"comparator", "$this$consume$iv", "iterator", "comparator", "$this$consume$iv", "iterator", "max"}, m167s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$maxWith$1 */
    static final class C11601<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C11601(Continuation<? super C11601> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.maxWith(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 0, 1, 1, 1, 1}, m164l = {464, 466}, m165m = "minWith", m166n = {"comparator", "$this$consume$iv", "iterator", "comparator", "$this$consume$iv", "iterator", "min"}, m167s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$minWith$1 */
    static final class C11611<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C11611(Continuation<? super C11611> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.minWith(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0}, m164l = {477}, m165m = "none", m166n = {"$this$consume$iv"}, m167s = {"L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$none$1 */
    static final class C11621<E> extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C11621(Continuation<? super C11621> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.none(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1}, m164l = {166, 169}, m165m = "single", m166n = {"$this$consume$iv", "iterator", "$this$consume$iv", "single"}, m167s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$single$1 */
    static final class C11641<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C11641(Continuation<? super C11641> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.single(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1}, m164l = {179, 182}, m165m = "singleOrNull", m166n = {"$this$consume$iv", "iterator", "$this$consume$iv", "single"}, m167s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$singleOrNull$1 */
    static final class C11651<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C11651(Continuation<? super C11651> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.singleOrNull(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1}, m164l = {517, 308}, m165m = "toChannel", m166n = {"destination", "$this$consume$iv$iv", "destination", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$toChannel$1 */
    static final class C11681<E, C extends SendChannel<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11681(Continuation<? super C11681> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toChannel(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0}, m164l = {517}, m165m = "toCollection", m166n = {"destination", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$toCollection$1 */
    static final class C11691<E, C extends Collection<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11691(Continuation<? super C11691> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toCollection(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m162f = "Deprecated.kt", m163i = {0, 0}, m164l = {517}, m165m = "toMap", m166n = {"destination", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$toMap$2 */
    static final class C11702<K, V, M extends Map<? super K, ? super V>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C11702(Continuation<? super C11702> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toMap(null, null, this);
        }
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    public static final <E, R> R consume(BroadcastChannel<E> broadcastChannel, Function1<? super ReceiveChannel<? extends E>, ? extends R> function1) {
        ReceiveChannel channel = broadcastChannel.openSubscription();
        try {
            return function1.invoke(channel);
        } finally {
            InlineMarker.finallyStart(1);
            ReceiveChannel.DefaultImpls.cancel$default(channel, (CancellationException) null, 1, (Object) null);
            InlineMarker.finallyEnd(1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x006f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0080 A[Catch: all -> 0x009e, TryCatch #2 {all -> 0x009e, blocks: (B:24:0x0078, B:26:0x0080, B:27:0x008e), top: B:40:0x0078 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008e A[Catch: all -> 0x009e, TRY_LEAVE, TryCatch #2 {all -> 0x009e, blocks: (B:24:0x0078, B:26:0x0080, B:27:0x008e), top: B:40:0x0078 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0070 -> B:40:0x0078). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.ERROR, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E> Object consumeEach(BroadcastChannel<E> broadcastChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) throws Throwable {
        C11351 c11351;
        ReceiveChannel channel$iv;
        Object $result;
        Function1 action;
        ReceiveChannel channel$iv2;
        ChannelIterator channelIterator;
        int i;
        Object obj;
        if (continuation instanceof C11351) {
            c11351 = (C11351) continuation;
            if ((c11351.label & Integer.MIN_VALUE) != 0) {
                c11351.label -= Integer.MIN_VALUE;
            } else {
                c11351 = new C11351(continuation);
            }
        }
        C11351 c113512 = c11351;
        Object element = c113512.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c113512.label) {
            case 0:
                ResultKt.throwOnFailure(element);
                channel$iv = broadcastChannel.openSubscription();
                try {
                    int $i$f$consumeEach = 0;
                    Function1 action2 = function1;
                    ChannelIterator it = channel$iv.iterator();
                    c113512.L$0 = action2;
                    c113512.L$1 = channel$iv;
                    c113512.L$2 = it;
                    c113512.label = 1;
                    Object objHasNext = it.hasNext(c113512);
                    if (objHasNext != $result2) {
                        return $result2;
                    }
                    Object obj2 = $result2;
                    $result = element;
                    element = objHasNext;
                    action = action2;
                    channel$iv2 = channel$iv;
                    channelIterator = it;
                    i = $i$f$consumeEach;
                    obj = obj2;
                    try {
                        if (((Boolean) element).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                            InlineMarker.finallyStart(1);
                            ReceiveChannel.DefaultImpls.cancel$default(channel$iv2, (CancellationException) null, 1, (Object) null);
                            InlineMarker.finallyEnd(1);
                            return Unit.INSTANCE;
                        }
                        action.invoke(channelIterator.next());
                        element = $result;
                        $result2 = obj;
                        $i$f$consumeEach = i;
                        it = channelIterator;
                        channel$iv = channel$iv2;
                        action2 = action;
                        c113512.L$0 = action2;
                        c113512.L$1 = channel$iv;
                        c113512.L$2 = it;
                        c113512.label = 1;
                        Object objHasNext2 = it.hasNext(c113512);
                        if (objHasNext2 != $result2) {
                        }
                    } catch (Throwable th) {
                        channel$iv = channel$iv2;
                        th = th;
                        InlineMarker.finallyStart(1);
                        ReceiveChannel.DefaultImpls.cancel$default(channel$iv, (CancellationException) null, 1, (Object) null);
                        InlineMarker.finallyEnd(1);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    InlineMarker.finallyStart(1);
                    ReceiveChannel.DefaultImpls.cancel$default(channel$iv, (CancellationException) null, 1, (Object) null);
                    InlineMarker.finallyEnd(1);
                    throw th;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c113512.L$2;
                channel$iv = (ReceiveChannel) c113512.L$1;
                Function1 action3 = (Function1) c113512.L$0;
                try {
                    ResultKt.throwOnFailure(element);
                    action = action3;
                    channel$iv2 = channel$iv;
                    channelIterator = channelIterator2;
                    i = 0;
                    obj = $result2;
                    $result = element;
                    if (((Boolean) element).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    th = th3;
                    InlineMarker.finallyStart(1);
                    ReceiveChannel.DefaultImpls.cancel$default(channel$iv, (CancellationException) null, 1, (Object) null);
                    InlineMarker.finallyEnd(1);
                    throw th;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "BroadcastChannel is deprecated in the favour of SharedFlow and is no longer supported")
    private static final <E> Object consumeEach$$forInline(BroadcastChannel<E> broadcastChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) {
        ReceiveChannel channel$iv = broadcastChannel.openSubscription();
        try {
            ReceiveChannel $this$consumeEach_u24lambda_u240 = channel$iv;
            ChannelIterator<E> it = $this$consumeEach_u24lambda_u240.iterator();
            while (true) {
                InlineMarker.mark(3);
                InlineMarker.mark(0);
                Object objHasNext = it.hasNext(null);
                InlineMarker.mark(1);
                if (!((Boolean) objHasNext).booleanValue()) {
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ReceiveChannel.DefaultImpls.cancel$default(channel$iv, (CancellationException) null, 1, (Object) null);
                    InlineMarker.finallyEnd(1);
                    return Unit.INSTANCE;
                }
                Object element = it.next();
                function1.invoke(element);
            }
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            ReceiveChannel.DefaultImpls.cancel$default(channel$iv, (CancellationException) null, 1, (Object) null);
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }

    public static final Function1<Throwable, Unit> consumesAll(final ReceiveChannel<?>... receiveChannelArr) {
        return new Function1<Throwable, Unit>() { // from class: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.consumesAll.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) throws Throwable {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(Throwable cause) throws Throwable {
                Throwable exception = null;
                for (ReceiveChannel channel : receiveChannelArr) {
                    try {
                        ChannelsKt.cancelConsumed(channel, cause);
                    } catch (Throwable e) {
                        if (exception == null) {
                            exception = e;
                        } else {
                            ExceptionsKt.addSuppressed(exception, e);
                        }
                    }
                }
                if (exception != null) {
                    Throwable it = exception;
                    throw it;
                }
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0079 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008b A[Catch: all -> 0x00c3, TRY_LEAVE, TryCatch #1 {all -> 0x00c3, blocks: (B:26:0x0083, B:28:0x008b), top: B:54:0x0083 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x007a -> B:54:0x0083). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object elementAt(ReceiveChannel $this$consume$iv, int index, Continuation continuation) {
        C11431 c11431;
        ReceiveChannel $this$consume$iv2;
        Object $result;
        Throwable th;
        ReceiveChannel $this$consume$iv3;
        ChannelIterator channelIterator;
        int index2;
        int index3;
        Object obj;
        if (continuation instanceof C11431) {
            c11431 = (C11431) continuation;
            if ((c11431.label & Integer.MIN_VALUE) != 0) {
                c11431.label -= Integer.MIN_VALUE;
            } else {
                c11431 = new C11431(continuation);
            }
        }
        C11431 c114312 = c11431;
        Object $result2 = c114312.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c114312.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                try {
                    if (index < 0) {
                        throw new IndexOutOfBoundsException("ReceiveChannel doesn't contain element at index " + index + '.');
                    }
                    ChannelIterator it = $this$consume$iv.iterator();
                    ReceiveChannel $this$consume$iv4 = $this$consume$iv;
                    int $i$f$consume = 0;
                    Throwable cause$iv = null;
                    int index4 = index;
                    try {
                        c114312.L$0 = $this$consume$iv4;
                        c114312.L$1 = it;
                        c114312.I$0 = index4;
                        c114312.I$1 = $i$f$consume;
                        c114312.label = 1;
                        Object objHasNext = it.hasNext(c114312);
                        if (objHasNext != $result3) {
                            return $result3;
                        }
                        Object obj2 = $result3;
                        $result = $result2;
                        $result2 = objHasNext;
                        th = cause$iv;
                        $this$consume$iv3 = $this$consume$iv4;
                        channelIterator = it;
                        index2 = index4;
                        index3 = $i$f$consume;
                        obj = obj2;
                        try {
                            if (((Boolean) $result2).booleanValue()) {
                                ReceiveChannel $this$consume$iv5 = $this$consume$iv3;
                                try {
                                    throw new IndexOutOfBoundsException("ReceiveChannel doesn't contain element at index " + index2 + '.');
                                } catch (Throwable th2) {
                                    e$iv = th2;
                                    $this$consume$iv2 = $this$consume$iv5;
                                    Throwable cause$iv2 = e$iv;
                                    try {
                                        throw e$iv;
                                    } catch (Throwable e$iv) {
                                        ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv2);
                                        throw e$iv;
                                    }
                                }
                            }
                            Object next = channelIterator.next();
                            int count = index3 + 1;
                            if (index2 != index3) {
                                ReceiveChannel $this$consume$iv6 = $this$consume$iv3;
                                cause$iv = th;
                                index4 = index2;
                                it = channelIterator;
                                $this$consume$iv4 = $this$consume$iv6;
                                $result2 = $result;
                                $result3 = obj;
                                $i$f$consume = count;
                                c114312.L$0 = $this$consume$iv4;
                                c114312.L$1 = it;
                                c114312.I$0 = index4;
                                c114312.I$1 = $i$f$consume;
                                c114312.label = 1;
                                Object objHasNext2 = it.hasNext(c114312);
                                if (objHasNext2 != $result3) {
                                }
                            } else {
                                ChannelsKt.cancelConsumed($this$consume$iv3, th);
                                return next;
                            }
                        } catch (Throwable th3) {
                            e$iv = th3;
                            $this$consume$iv2 = $this$consume$iv3;
                        }
                    } catch (Throwable th4) {
                        e$iv = th4;
                        $this$consume$iv2 = $this$consume$iv4;
                        Throwable cause$iv22 = e$iv;
                        throw e$iv;
                    }
                } catch (Throwable th5) {
                    e$iv = th5;
                    $this$consume$iv2 = $this$consume$iv;
                    Throwable cause$iv222 = e$iv;
                    throw e$iv;
                }
            case 1:
                int count2 = c114312.I$1;
                int index5 = c114312.I$0;
                ChannelIterator channelIterator2 = (ChannelIterator) c114312.L$1;
                ReceiveChannel receiveChannel = (ReceiveChannel) c114312.L$0;
                try {
                    ResultKt.throwOnFailure($result2);
                    th = null;
                    $this$consume$iv3 = receiveChannel;
                    channelIterator = channelIterator2;
                    index2 = index5;
                    index3 = count2;
                    obj = $result3;
                    $result = $result2;
                    if (((Boolean) $result2).booleanValue()) {
                    }
                } catch (Throwable th6) {
                    e$iv = th6;
                    $this$consume$iv2 = receiveChannel;
                    Throwable cause$iv2222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x007a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x008b A[Catch: all -> 0x00a5, TRY_LEAVE, TryCatch #1 {all -> 0x00a5, blocks: (B:28:0x0083, B:30:0x008b), top: B:51:0x0083 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x007b -> B:51:0x0083). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object elementAtOrNull(ReceiveChannel $this$consume$iv, int index, Continuation continuation) {
        C11441 c11441;
        ReceiveChannel $this$consume$iv2;
        Throwable e$iv;
        int index2;
        int $i$f$consume;
        ReceiveChannel $this$consume$iv3;
        ChannelIterator it;
        Object $result;
        Throwable th;
        ChannelIterator channelIterator;
        int index3;
        int index4;
        Object obj;
        if (continuation instanceof C11441) {
            c11441 = (C11441) continuation;
            if ((c11441.label & Integer.MIN_VALUE) != 0) {
                c11441.label -= Integer.MIN_VALUE;
            } else {
                c11441 = new C11441(continuation);
            }
        }
        C11441 c114412 = c11441;
        Object $result2 = c114412.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c114412.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                if (index < 0) {
                    ChannelsKt.cancelConsumed($this$consume$iv, null);
                    return null;
                }
                Throwable cause$iv = null;
                try {
                    index2 = index;
                    $i$f$consume = 0;
                    $this$consume$iv3 = $this$consume$iv;
                    it = $this$consume$iv.iterator();
                } catch (Throwable th2) {
                    $this$consume$iv2 = $this$consume$iv;
                    e$iv = th2;
                    Throwable cause$iv2 = e$iv;
                    try {
                        throw e$iv;
                    } catch (Throwable e$iv2) {
                        ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv2);
                        throw e$iv2;
                    }
                }
                try {
                    c114412.L$0 = $this$consume$iv3;
                    c114412.L$1 = it;
                    c114412.I$0 = index2;
                    c114412.I$1 = $i$f$consume;
                    c114412.label = 1;
                    Object objHasNext = it.hasNext(c114412);
                    if (objHasNext != $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result = $result2;
                    $result2 = objHasNext;
                    th = cause$iv;
                    channelIterator = it;
                    index3 = index2;
                    index4 = $i$f$consume;
                    obj = obj2;
                    try {
                        if (((Boolean) $result2).booleanValue()) {
                            ChannelsKt.cancelConsumed($this$consume$iv3, th);
                            return null;
                        }
                        Object next = channelIterator.next();
                        int count = index4 + 1;
                        if (index3 == index4) {
                            ChannelsKt.cancelConsumed($this$consume$iv3, th);
                            return next;
                        }
                        index2 = index3;
                        it = channelIterator;
                        cause$iv = th;
                        $result2 = $result;
                        $result3 = obj;
                        $i$f$consume = count;
                        c114412.L$0 = $this$consume$iv3;
                        c114412.L$1 = it;
                        c114412.I$0 = index2;
                        c114412.I$1 = $i$f$consume;
                        c114412.label = 1;
                        Object objHasNext2 = it.hasNext(c114412);
                        if (objHasNext2 != $result3) {
                        }
                    } catch (Throwable th3) {
                        e$iv = th3;
                        $this$consume$iv2 = $this$consume$iv3;
                        Throwable cause$iv22 = e$iv;
                        throw e$iv;
                    }
                } catch (Throwable th4) {
                    e$iv = th4;
                    $this$consume$iv2 = $this$consume$iv3;
                    Throwable cause$iv222 = e$iv;
                    throw e$iv;
                }
            case 1:
                int count2 = c114412.I$1;
                int index5 = c114412.I$0;
                ChannelIterator channelIterator2 = (ChannelIterator) c114412.L$1;
                ReceiveChannel receiveChannel = (ReceiveChannel) c114412.L$0;
                try {
                    ResultKt.throwOnFailure($result2);
                    th = null;
                    $this$consume$iv3 = receiveChannel;
                    channelIterator = channelIterator2;
                    index3 = index5;
                    index4 = count2;
                    obj = $result3;
                    $result = $result2;
                    if (((Boolean) $result2).booleanValue()) {
                    }
                } catch (Throwable th5) {
                    e$iv = th5;
                    $this$consume$iv2 = receiveChannel;
                    Throwable cause$iv2222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0067 A[Catch: all -> 0x003d, TRY_LEAVE, TryCatch #2 {all -> 0x003d, blocks: (B:13:0x0038, B:22:0x005f, B:24:0x0067, B:27:0x006f, B:28:0x0076), top: B:40:0x0038 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006f A[Catch: all -> 0x003d, TRY_ENTER, TryCatch #2 {all -> 0x003d, blocks: (B:13:0x0038, B:22:0x005f, B:24:0x0067, B:27:0x006f, B:28:0x0076), top: B:40:0x0038 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object first(ReceiveChannel $this$first, Continuation continuation) {
        C11511 c11511;
        ReceiveChannel $this$consume$iv;
        Throwable cause$iv;
        ReceiveChannel $this$consume$iv2;
        ChannelIterator iterator;
        Object objHasNext;
        if (continuation instanceof C11511) {
            c11511 = (C11511) continuation;
            if ((c11511.label & Integer.MIN_VALUE) != 0) {
                c11511.label -= Integer.MIN_VALUE;
            } else {
                c11511 = new C11511(continuation);
            }
        }
        C11511 c115112 = c11511;
        Object $result = c115112.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c115112.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$consume$iv = $this$first;
                cause$iv = null;
                try {
                    iterator = $this$consume$iv.iterator();
                    c115112.L$0 = $this$consume$iv;
                    c115112.L$1 = iterator;
                    c115112.label = 1;
                    objHasNext = iterator.hasNext(c115112);
                    if (objHasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (((Boolean) objHasNext).booleanValue()) {
                        throw new NoSuchElementException("ReceiveChannel is empty.");
                    }
                    Object next = iterator.next();
                    ChannelsKt.cancelConsumed($this$consume$iv, cause$iv);
                    return next;
                } catch (Throwable th) {
                    e$iv = th;
                    $this$consume$iv2 = $this$consume$iv;
                    Throwable cause$iv2 = e$iv;
                    try {
                        throw e$iv;
                    } catch (Throwable e$iv) {
                        ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv2);
                        throw e$iv;
                    }
                }
            case 1:
                iterator = (ChannelIterator) c115112.L$1;
                $this$consume$iv = (ReceiveChannel) c115112.L$0;
                cause$iv = null;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext = $result;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                } catch (Throwable th2) {
                    e$iv = th2;
                    $this$consume$iv2 = $this$consume$iv;
                    Throwable cause$iv22 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x006d A[Catch: all -> 0x0072, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0072, blocks: (B:22:0x0060, B:26:0x006d), top: B:37:0x0060 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object firstOrNull(ReceiveChannel $this$firstOrNull, Continuation continuation) {
        C11521 c11521;
        ReceiveChannel $this$consume$iv;
        Throwable cause$iv;
        ReceiveChannel $this$consume$iv2;
        ChannelIterator iterator;
        Object objHasNext;
        if (continuation instanceof C11521) {
            c11521 = (C11521) continuation;
            if ((c11521.label & Integer.MIN_VALUE) != 0) {
                c11521.label -= Integer.MIN_VALUE;
            } else {
                c11521 = new C11521(continuation);
            }
        }
        C11521 c115212 = c11521;
        Object $result = c115212.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c115212.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$consume$iv = $this$firstOrNull;
                cause$iv = null;
                try {
                    iterator = $this$consume$iv.iterator();
                    c115212.L$0 = $this$consume$iv;
                    c115212.L$1 = iterator;
                    c115212.label = 1;
                    objHasNext = iterator.hasNext(c115212);
                    if (objHasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    try {
                        Object next = ((Boolean) objHasNext).booleanValue() ? iterator.next() : null;
                        ChannelsKt.cancelConsumed($this$consume$iv, cause$iv);
                        return next;
                    } catch (Throwable th) {
                        e$iv = th;
                        $this$consume$iv2 = $this$consume$iv;
                        Throwable cause$iv2 = e$iv;
                        try {
                            throw e$iv;
                        } catch (Throwable e$iv) {
                            ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv2);
                            throw e$iv;
                        }
                    }
                } catch (Throwable th2) {
                    e$iv = th2;
                    $this$consume$iv2 = $this$consume$iv;
                    Throwable cause$iv22 = e$iv;
                    throw e$iv;
                }
            case 1:
                ChannelIterator iterator2 = (ChannelIterator) c115212.L$1;
                $this$consume$iv = (ReceiveChannel) c115212.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext = $result;
                    iterator = iterator2;
                    cause$iv = null;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                    ChannelsKt.cancelConsumed($this$consume$iv, cause$iv);
                    return next;
                } catch (Throwable th3) {
                    e$iv = th3;
                    $this$consume$iv2 = $this$consume$iv;
                    Throwable cause$iv222 = e$iv;
                    throw e$iv;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0088 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x009e A[Catch: all -> 0x00ed, TryCatch #5 {all -> 0x00ed, blocks: (B:24:0x0096, B:26:0x009e, B:28:0x00aa), top: B:63:0x0096 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0016  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0089 -> B:63:0x0096). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object indexOf(ReceiveChannel $this$consume$iv$iv, Object element, Continuation continuation) {
        C11541 c11541;
        ReceiveChannel $this$consume$iv$iv2;
        Object element2;
        Object element3;
        ReceiveChannel $this$consume$iv$iv3;
        Object $result;
        ChannelIterator it;
        Ref.IntRef index;
        Ref.IntRef index2;
        Throwable cause$iv$iv;
        Object objHasNext;
        Object $result2;
        Throwable th;
        Object element4;
        Ref.IntRef index3;
        ReceiveChannel receiveChannel;
        ChannelIterator channelIterator;
        Ref.IntRef intRef;
        Object obj;
        Object obj2;
        if (continuation instanceof C11541) {
            c11541 = (C11541) continuation;
            if ((c11541.label & Integer.MIN_VALUE) != 0) {
                c11541.label -= Integer.MIN_VALUE;
            } else {
                c11541 = new C11541(continuation);
            }
        }
        Object $result3 = c11541.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c11541.label) {
            case 0:
                ResultKt.throwOnFailure($result3);
                Ref.IntRef index4 = new Ref.IntRef();
                try {
                    element2 = element;
                    element3 = null;
                    $this$consume$iv$iv3 = $this$consume$iv$iv;
                    $result = null;
                    it = $this$consume$iv$iv.iterator();
                    index = index4;
                    index2 = null;
                    cause$iv$iv = null;
                    try {
                        c11541.L$0 = element2;
                        c11541.L$1 = index;
                        c11541.L$2 = $this$consume$iv$iv3;
                        c11541.L$3 = it;
                        c11541.label = 1;
                        objHasNext = it.hasNext(c11541);
                    } catch (Throwable th2) {
                        e$iv$iv = th2;
                        $this$consume$iv$iv2 = $this$consume$iv$iv3;
                    }
                } catch (Throwable th3) {
                    e$iv$iv = th3;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                }
                if (objHasNext != coroutine_suspended) {
                    return coroutine_suspended;
                }
                Object obj3 = $result;
                $result2 = $result3;
                $result3 = objHasNext;
                th = cause$iv$iv;
                element4 = element2;
                index3 = index;
                receiveChannel = $this$consume$iv$iv3;
                channelIterator = it;
                intRef = index2;
                obj = element3;
                obj2 = obj3;
                try {
                    if (((Boolean) $result3).booleanValue()) {
                        $this$consume$iv$iv2 = receiveChannel;
                        Throwable cause$iv$iv2 = th;
                        try {
                            Unit unit = Unit.INSTANCE;
                            ChannelsKt.cancelConsumed($this$consume$iv$iv2, cause$iv$iv2);
                            return Boxing.boxInt(-1);
                        } catch (Throwable th4) {
                            e$iv$iv = th4;
                        }
                    } else {
                        Object e$iv = channelIterator.next();
                        if (Intrinsics.areEqual(element4, e$iv)) {
                            Integer numBoxInt = Boxing.boxInt(index3.element);
                            ChannelsKt.cancelConsumed(receiveChannel, th);
                            return numBoxInt;
                        }
                        ReceiveChannel $this$consume$iv$iv4 = receiveChannel;
                        Throwable cause$iv$iv3 = th;
                        try {
                            index3.element++;
                            ChannelIterator channelIterator2 = channelIterator;
                            $this$consume$iv$iv3 = $this$consume$iv$iv4;
                            $result3 = $result2;
                            $result = obj2;
                            element3 = obj;
                            index2 = intRef;
                            it = channelIterator2;
                            Object obj4 = element4;
                            cause$iv$iv = cause$iv$iv3;
                            index = index3;
                            element2 = obj4;
                            c11541.L$0 = element2;
                            c11541.L$1 = index;
                            c11541.L$2 = $this$consume$iv$iv3;
                            c11541.L$3 = it;
                            c11541.label = 1;
                            objHasNext = it.hasNext(c11541);
                            if (objHasNext != coroutine_suspended) {
                            }
                        } catch (Throwable th5) {
                            e$iv$iv = th5;
                            $this$consume$iv$iv2 = $this$consume$iv$iv4;
                        }
                    }
                } catch (Throwable th6) {
                    e$iv$iv = th6;
                    $this$consume$iv$iv2 = receiveChannel;
                }
                Throwable cause$iv$iv4 = e$iv$iv;
                try {
                    throw e$iv$iv;
                } catch (Throwable e$iv$iv) {
                    ChannelsKt.cancelConsumed($this$consume$iv$iv2, cause$iv$iv4);
                    throw e$iv$iv;
                }
            case 1:
                ChannelIterator channelIterator3 = (ChannelIterator) c11541.L$3;
                ReceiveChannel receiveChannel2 = (ReceiveChannel) c11541.L$2;
                Ref.IntRef index5 = (Ref.IntRef) c11541.L$1;
                Object element5 = c11541.L$0;
                try {
                    ResultKt.throwOnFailure($result3);
                    th = null;
                    element4 = element5;
                    index3 = index5;
                    receiveChannel = receiveChannel2;
                    channelIterator = channelIterator3;
                    intRef = null;
                    obj = null;
                    obj2 = null;
                    $result2 = $result3;
                    if (((Boolean) $result3).booleanValue()) {
                    }
                } catch (Throwable th7) {
                    e$iv$iv = th7;
                    $this$consume$iv$iv2 = receiveChannel2;
                }
                Throwable cause$iv$iv42 = e$iv$iv;
                throw e$iv$iv;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x008b A[Catch: all -> 0x00de, TRY_LEAVE, TryCatch #5 {all -> 0x00de, blocks: (B:27:0x0083, B:29:0x008b, B:45:0x00d6, B:46:0x00dd), top: B:65:0x0083 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00a5 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b7 A[Catch: all -> 0x00cb, TRY_LEAVE, TryCatch #4 {all -> 0x00cb, blocks: (B:35:0x00af, B:37:0x00b7), top: B:63:0x00af }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d6 A[Catch: all -> 0x00de, TRY_ENTER, TryCatch #5 {all -> 0x00de, blocks: (B:27:0x0083, B:29:0x008b, B:45:0x00d6, B:46:0x00dd), top: B:65:0x0083 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x00a6 -> B:63:0x00af). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object last(ReceiveChannel $this$last, Continuation continuation) {
        C11551 c11551;
        ReceiveChannel $this$consume$iv;
        int i;
        Object objHasNext;
        Throwable cause$iv;
        ChannelIterator iterator;
        Object $result;
        Throwable th;
        ReceiveChannel receiveChannel;
        ChannelIterator iterator2;
        Object obj;
        int i2;
        Object obj2;
        if (continuation instanceof C11551) {
            c11551 = (C11551) continuation;
            if ((c11551.label & Integer.MIN_VALUE) != 0) {
                c11551.label -= Integer.MIN_VALUE;
            } else {
                c11551 = new C11551(continuation);
            }
        }
        C11551 c115512 = c11551;
        Object last = c115512.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c115512.label) {
            case 0:
                ResultKt.throwOnFailure(last);
                $this$consume$iv = $this$last;
                i = 0;
                try {
                    ChannelIterator iterator3 = $this$consume$iv.iterator();
                    c115512.L$0 = $this$consume$iv;
                    c115512.L$1 = iterator3;
                    c115512.label = 1;
                    objHasNext = iterator3.hasNext(c115512);
                    if (objHasNext == $result2) {
                        return $result2;
                    }
                    cause$iv = null;
                    iterator = iterator3;
                    try {
                        if (((Boolean) objHasNext).booleanValue()) {
                            throw new NoSuchElementException("ReceiveChannel is empty.");
                        }
                        int i3 = i;
                        ReceiveChannel $this$consume$iv2 = $this$consume$iv;
                        int i4 = i3;
                        Throwable th2 = cause$iv;
                        ChannelIterator iterator4 = iterator;
                        Object last2 = iterator.next();
                        Throwable cause$iv2 = th2;
                        try {
                            c115512.L$0 = $this$consume$iv2;
                            c115512.L$1 = iterator4;
                            c115512.L$2 = last2;
                            c115512.label = 2;
                            Object objHasNext2 = iterator4.hasNext(c115512);
                            if (objHasNext2 != $result2) {
                                return $result2;
                            }
                            Object obj3 = $result2;
                            $result = last;
                            last = objHasNext2;
                            th = cause$iv2;
                            receiveChannel = $this$consume$iv2;
                            iterator2 = iterator4;
                            obj = last2;
                            i2 = i4;
                            obj2 = obj3;
                            try {
                                if (((Boolean) last).booleanValue()) {
                                    ChannelsKt.cancelConsumed(receiveChannel, th);
                                    return obj;
                                }
                                ReceiveChannel $this$consume$iv3 = receiveChannel;
                                cause$iv2 = th;
                                int i5 = i2;
                                last2 = iterator2.next();
                                last = $result;
                                $result2 = obj2;
                                i4 = i5;
                                ChannelIterator channelIterator = iterator2;
                                $this$consume$iv2 = $this$consume$iv3;
                                iterator4 = channelIterator;
                                c115512.L$0 = $this$consume$iv2;
                                c115512.L$1 = iterator4;
                                c115512.L$2 = last2;
                                c115512.label = 2;
                                Object objHasNext22 = iterator4.hasNext(c115512);
                                if (objHasNext22 != $result2) {
                                }
                            } catch (Throwable th3) {
                                $this$consume$iv = receiveChannel;
                                e$iv = th3;
                                Throwable cause$iv3 = e$iv;
                                try {
                                    throw e$iv;
                                } catch (Throwable e$iv) {
                                    ChannelsKt.cancelConsumed($this$consume$iv, cause$iv3);
                                    throw e$iv;
                                }
                            }
                        } catch (Throwable th4) {
                            e$iv = th4;
                            $this$consume$iv = $this$consume$iv2;
                            Throwable cause$iv32 = e$iv;
                            throw e$iv;
                        }
                    } catch (Throwable th5) {
                        e$iv = th5;
                        Throwable cause$iv322 = e$iv;
                        throw e$iv;
                    }
                } catch (Throwable th6) {
                    e$iv = th6;
                    Throwable cause$iv3222 = e$iv;
                    throw e$iv;
                }
            case 1:
                iterator = (ChannelIterator) c115512.L$1;
                cause$iv = null;
                ReceiveChannel $this$consume$iv4 = (ReceiveChannel) c115512.L$0;
                try {
                    ResultKt.throwOnFailure(last);
                    objHasNext = last;
                    i = 0;
                    $this$consume$iv = $this$consume$iv4;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                } catch (Throwable th7) {
                    e$iv = th7;
                    $this$consume$iv = $this$consume$iv4;
                    Throwable cause$iv32222 = e$iv;
                    throw e$iv;
                }
                break;
            case 2:
                Object last3 = c115512.L$2;
                ChannelIterator iterator5 = (ChannelIterator) c115512.L$1;
                ReceiveChannel receiveChannel2 = (ReceiveChannel) c115512.L$0;
                try {
                    ResultKt.throwOnFailure(last);
                    th = null;
                    receiveChannel = receiveChannel2;
                    iterator2 = iterator5;
                    obj = last3;
                    i2 = 0;
                    obj2 = $result2;
                    $result = last;
                    if (((Boolean) last).booleanValue()) {
                    }
                } catch (Throwable th8) {
                    e$iv = th8;
                    $this$consume$iv = receiveChannel2;
                    Throwable cause$iv322222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0092 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00a9 A[Catch: all -> 0x00d9, TryCatch #3 {all -> 0x00d9, blocks: (B:24:0x00a1, B:26:0x00a9, B:28:0x00b4, B:29:0x00b8, B:30:0x00c9), top: B:48:0x00a1 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c9 A[Catch: all -> 0x00d9, TRY_LEAVE, TryCatch #3 {all -> 0x00d9, blocks: (B:24:0x00a1, B:26:0x00a9, B:28:0x00b4, B:29:0x00b8, B:30:0x00c9), top: B:48:0x00a1 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0016  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0093 -> B:48:0x00a1). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object lastIndexOf(ReceiveChannel $this$consumeEach$iv, Object element, Continuation continuation) {
        C11561 c11561;
        ReceiveChannel $this$consume$iv$iv;
        Object $result;
        Object element2;
        Ref.IntRef lastIndex;
        Ref.IntRef lastIndex2;
        ReceiveChannel $this$consume$iv$iv2;
        Throwable cause$iv$iv;
        ChannelIterator channelIterator;
        Ref.IntRef intRef;
        Object obj;
        int $i$f$consume;
        if (continuation instanceof C11561) {
            c11561 = (C11561) continuation;
            if ((c11561.label & Integer.MIN_VALUE) != 0) {
                c11561.label -= Integer.MIN_VALUE;
            } else {
                c11561 = new C11561(continuation);
            }
        }
        Object $result2 = c11561.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c11561.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                Ref.IntRef lastIndex3 = new Ref.IntRef();
                lastIndex3.element = -1;
                Ref.IntRef index = new Ref.IntRef();
                $this$consume$iv$iv = $this$consumeEach$iv;
                try {
                    Ref.IntRef index2 = lastIndex3;
                    Ref.IntRef lastIndex4 = null;
                    Object element3 = element;
                    Object element4 = null;
                    int $i$f$consume2 = 0;
                    Throwable cause$iv$iv2 = null;
                    Ref.IntRef index3 = index;
                    ChannelIterator it = $this$consume$iv$iv.iterator();
                    c11561.L$0 = element3;
                    c11561.L$1 = index2;
                    c11561.L$2 = index3;
                    c11561.L$3 = $this$consume$iv$iv;
                    c11561.L$4 = it;
                    c11561.label = 1;
                    Object objHasNext = it.hasNext(c11561);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    int i = $i$f$consume2;
                    $result = $result2;
                    $result2 = objHasNext;
                    element2 = element3;
                    lastIndex = index2;
                    lastIndex2 = index3;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv = cause$iv$iv2;
                    channelIterator = it;
                    intRef = lastIndex4;
                    obj = element4;
                    $i$f$consume = i;
                    try {
                        if (!((Boolean) $result2).booleanValue()) {
                            Object it2 = channelIterator.next();
                            if (Intrinsics.areEqual(element2, it2)) {
                                lastIndex.element = lastIndex2.element;
                            }
                            lastIndex2.element++;
                            $result2 = $result;
                            $i$f$consume2 = $i$f$consume;
                            element4 = obj;
                            lastIndex4 = intRef;
                            it = channelIterator;
                            cause$iv$iv2 = cause$iv$iv;
                            $this$consume$iv$iv = $this$consume$iv$iv2;
                            index3 = lastIndex2;
                            index2 = lastIndex;
                            element3 = element2;
                            c11561.L$0 = element3;
                            c11561.L$1 = index2;
                            c11561.L$2 = index3;
                            c11561.L$3 = $this$consume$iv$iv;
                            c11561.L$4 = it;
                            c11561.label = 1;
                            Object objHasNext2 = it.hasNext(c11561);
                            if (objHasNext2 != coroutine_suspended) {
                            }
                        } else {
                            Unit unit = Unit.INSTANCE;
                            ChannelsKt.cancelConsumed($this$consume$iv$iv2, cause$iv$iv);
                            return Boxing.boxInt(lastIndex.element);
                        }
                    } catch (Throwable th) {
                        e$iv$iv = th;
                        $this$consume$iv$iv = $this$consume$iv$iv2;
                        Throwable cause$iv$iv3 = e$iv$iv;
                        try {
                            throw e$iv$iv;
                        } catch (Throwable e$iv$iv) {
                            ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv3);
                            throw e$iv$iv;
                        }
                    }
                } catch (Throwable th2) {
                    e$iv$iv = th2;
                    Throwable cause$iv$iv32 = e$iv$iv;
                    throw e$iv$iv;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c11561.L$4;
                $this$consume$iv$iv = (ReceiveChannel) c11561.L$3;
                Ref.IntRef index4 = (Ref.IntRef) c11561.L$2;
                Ref.IntRef lastIndex5 = (Ref.IntRef) c11561.L$1;
                Object element5 = c11561.L$0;
                try {
                    ResultKt.throwOnFailure($result2);
                    element2 = element5;
                    lastIndex = lastIndex5;
                    lastIndex2 = index4;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv = null;
                    channelIterator = channelIterator2;
                    intRef = null;
                    obj = null;
                    $i$f$consume = 0;
                    $result = $result2;
                    if (!((Boolean) $result2).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv$iv = th3;
                    Throwable cause$iv$iv322 = e$iv$iv;
                    throw e$iv$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b6 A[Catch: all -> 0x00ca, TRY_LEAVE, TryCatch #5 {all -> 0x00ca, blocks: (B:39:0x00ae, B:41:0x00b6), top: B:70:0x00ae }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x00a5 -> B:70:0x00ae). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object lastOrNull(ReceiveChannel $this$consume$iv, Continuation continuation) {
        C11571 c11571;
        int $i$f$consume;
        Throwable cause$iv;
        ReceiveChannel $this$consume$iv2;
        Throwable e$iv;
        ChannelIterator iterator;
        Object objHasNext;
        ReceiveChannel receiveChannel;
        Object $result;
        ReceiveChannel $this$consume$iv3;
        ChannelIterator iterator2;
        Object obj;
        Throwable th;
        int $i$f$consume2;
        Object obj2;
        if (continuation instanceof C11571) {
            c11571 = (C11571) continuation;
            if ((c11571.label & Integer.MIN_VALUE) != 0) {
                c11571.label -= Integer.MIN_VALUE;
            } else {
                c11571 = new C11571(continuation);
            }
        }
        C11571 c115712 = c11571;
        Object last = c115712.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c115712.label) {
            case 0:
                ResultKt.throwOnFailure(last);
                $i$f$consume = 0;
                cause$iv = null;
                try {
                    iterator = $this$consume$iv.iterator();
                    c115712.L$0 = $this$consume$iv;
                    c115712.L$1 = iterator;
                    c115712.label = 1;
                    objHasNext = iterator.hasNext(c115712);
                    if (objHasNext == $result2) {
                        return $result2;
                    }
                    receiveChannel = $this$consume$iv;
                    try {
                        if (((Boolean) objHasNext).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel, cause$iv);
                            return null;
                        }
                        ReceiveChannel $this$consume$iv4 = receiveChannel;
                        try {
                            ReceiveChannel $this$consume$iv5 = $this$consume$iv4;
                            Throwable cause$iv2 = cause$iv;
                            Object last2 = iterator.next();
                            try {
                                c115712.L$0 = $this$consume$iv5;
                                c115712.L$1 = iterator;
                                c115712.L$2 = last2;
                                c115712.label = 2;
                                Object objHasNext2 = iterator.hasNext(c115712);
                                if (objHasNext2 != $result2) {
                                    return $result2;
                                }
                                Object obj3 = $result2;
                                $result = last;
                                last = objHasNext2;
                                $this$consume$iv3 = $this$consume$iv5;
                                iterator2 = iterator;
                                obj = last2;
                                th = cause$iv2;
                                $i$f$consume2 = $i$f$consume;
                                obj2 = obj3;
                                try {
                                    if (((Boolean) last).booleanValue()) {
                                        ChannelsKt.cancelConsumed($this$consume$iv3, th);
                                        return obj;
                                    }
                                    Throwable th2 = th;
                                    last2 = iterator2.next();
                                    last = $result;
                                    $result2 = obj2;
                                    $i$f$consume = $i$f$consume2;
                                    cause$iv2 = th2;
                                    ChannelIterator channelIterator = iterator2;
                                    $this$consume$iv5 = $this$consume$iv3;
                                    iterator = channelIterator;
                                    c115712.L$0 = $this$consume$iv5;
                                    c115712.L$1 = iterator;
                                    c115712.L$2 = last2;
                                    c115712.label = 2;
                                    Object objHasNext22 = iterator.hasNext(c115712);
                                    if (objHasNext22 != $result2) {
                                    }
                                } catch (Throwable th3) {
                                    e$iv = th3;
                                    $this$consume$iv2 = $this$consume$iv3;
                                    Throwable cause$iv3 = e$iv;
                                    try {
                                        throw e$iv;
                                    } catch (Throwable e$iv2) {
                                        ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv3);
                                        throw e$iv2;
                                    }
                                }
                            } catch (Throwable th4) {
                                e$iv = th4;
                                $this$consume$iv2 = $this$consume$iv5;
                                Throwable cause$iv32 = e$iv;
                                throw e$iv;
                            }
                        } catch (Throwable th5) {
                            e$iv = th5;
                            $this$consume$iv2 = $this$consume$iv4;
                            Throwable cause$iv322 = e$iv;
                            throw e$iv;
                        }
                    } catch (Throwable th6) {
                        e$iv = th6;
                        $this$consume$iv2 = receiveChannel;
                    }
                } catch (Throwable th7) {
                    $this$consume$iv2 = $this$consume$iv;
                    e$iv = th7;
                    Throwable cause$iv3222 = e$iv;
                    throw e$iv;
                }
            case 1:
                $i$f$consume = 0;
                ChannelIterator iterator3 = (ChannelIterator) c115712.L$1;
                ReceiveChannel receiveChannel2 = (ReceiveChannel) c115712.L$0;
                try {
                    ResultKt.throwOnFailure(last);
                    objHasNext = last;
                    receiveChannel = receiveChannel2;
                    iterator = iterator3;
                    cause$iv = null;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                } catch (Throwable th8) {
                    e$iv = th8;
                    $this$consume$iv2 = receiveChannel2;
                    Throwable cause$iv32222 = e$iv;
                    throw e$iv;
                }
                break;
            case 2:
                Object last3 = c115712.L$2;
                ChannelIterator iterator4 = (ChannelIterator) c115712.L$1;
                ReceiveChannel receiveChannel3 = (ReceiveChannel) c115712.L$0;
                try {
                    ResultKt.throwOnFailure(last);
                    $this$consume$iv3 = receiveChannel3;
                    iterator2 = iterator4;
                    obj = last3;
                    th = null;
                    $i$f$consume2 = 0;
                    obj2 = $result2;
                    $result = last;
                    if (((Boolean) last).booleanValue()) {
                    }
                } catch (Throwable th9) {
                    e$iv = th9;
                    $this$consume$iv2 = receiveChannel3;
                    Throwable cause$iv322222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0080 A[Catch: all -> 0x0051, TRY_LEAVE, TryCatch #0 {all -> 0x0051, blocks: (B:18:0x004c, B:27:0x0078, B:29:0x0080, B:39:0x00aa, B:40:0x00b1), top: B:47:0x004c }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a2 A[Catch: all -> 0x003c, TRY_ENTER, TryCatch #3 {all -> 0x003c, blocks: (B:13:0x0036, B:33:0x0095, B:37:0x00a2, B:38:0x00a9), top: B:52:0x0036 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00aa A[Catch: all -> 0x0051, TRY_ENTER, TryCatch #0 {all -> 0x0051, blocks: (B:18:0x004c, B:27:0x0078, B:29:0x0080, B:39:0x00aa, B:40:0x00b1), top: B:47:0x004c }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object single(ReceiveChannel $this$single, Continuation continuation) {
        C11641 c11641;
        ReceiveChannel $this$consume$iv;
        Object objHasNext;
        ReceiveChannel $this$consume$iv2;
        ReceiveChannel $this$consume$iv3;
        Throwable cause$iv;
        ChannelIterator iterator;
        Object objHasNext2;
        ReceiveChannel receiveChannel;
        Object obj;
        if (continuation instanceof C11641) {
            c11641 = (C11641) continuation;
            if ((c11641.label & Integer.MIN_VALUE) != 0) {
                c11641.label -= Integer.MIN_VALUE;
            } else {
                c11641 = new C11641(continuation);
            }
        }
        C11641 c116412 = c11641;
        Object $result = c116412.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c116412.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$consume$iv = $this$single;
                try {
                    ChannelIterator iterator2 = $this$consume$iv.iterator();
                    c116412.L$0 = $this$consume$iv;
                    c116412.L$1 = iterator2;
                    c116412.label = 1;
                    objHasNext = iterator2.hasNext(c116412);
                    if (objHasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    $this$consume$iv2 = $this$consume$iv;
                    $this$consume$iv3 = null;
                    cause$iv = null;
                    iterator = iterator2;
                    if (((Boolean) objHasNext).booleanValue()) {
                        throw new NoSuchElementException("ReceiveChannel is empty.");
                    }
                    Object single = iterator.next();
                    c116412.L$0 = $this$consume$iv2;
                    c116412.L$1 = single;
                    c116412.label = 2;
                    objHasNext2 = iterator.hasNext(c116412);
                    if (objHasNext2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    receiveChannel = $this$consume$iv2;
                    obj = single;
                    if (!((Boolean) objHasNext2).booleanValue()) {
                        throw new IllegalArgumentException("ReceiveChannel has more than one element.");
                    }
                    ChannelsKt.cancelConsumed(receiveChannel, cause$iv);
                    return obj;
                } catch (Throwable th) {
                    e$iv = th;
                    Throwable cause$iv2 = e$iv;
                    try {
                        throw e$iv;
                    } catch (Throwable e$iv) {
                        ChannelsKt.cancelConsumed($this$consume$iv, cause$iv2);
                        throw e$iv;
                    }
                }
            case 1:
                $this$consume$iv3 = null;
                iterator = (ChannelIterator) c116412.L$1;
                cause$iv = null;
                $this$consume$iv2 = (ReceiveChannel) c116412.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext = $result;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                } catch (Throwable th2) {
                    e$iv = th2;
                    $this$consume$iv = $this$consume$iv2;
                    Throwable cause$iv22 = e$iv;
                    throw e$iv;
                }
                break;
            case 2:
                obj = c116412.L$1;
                receiveChannel = (ReceiveChannel) c116412.L$0;
                cause$iv = null;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext2 = $result;
                    if (!((Boolean) objHasNext2).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv = th3;
                    $this$consume$iv = receiveChannel;
                    Throwable cause$iv222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x008b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object singleOrNull(ReceiveChannel $this$singleOrNull, Continuation continuation) {
        C11651 c11651;
        ReceiveChannel $this$consume$iv;
        Throwable cause$iv;
        Throwable e$iv;
        ChannelIterator iterator;
        Object objHasNext;
        ReceiveChannel $this$consume$iv2;
        ReceiveChannel $this$consume$iv3;
        Object objHasNext2;
        Throwable th;
        ReceiveChannel receiveChannel;
        Object obj;
        if (continuation instanceof C11651) {
            c11651 = (C11651) continuation;
            if ((c11651.label & Integer.MIN_VALUE) != 0) {
                c11651.label -= Integer.MIN_VALUE;
            } else {
                c11651 = new C11651(continuation);
            }
        }
        C11651 c116512 = c11651;
        Object $result = c116512.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c116512.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$consume$iv = $this$singleOrNull;
                cause$iv = null;
                try {
                    iterator = $this$consume$iv.iterator();
                    c116512.L$0 = $this$consume$iv;
                    c116512.L$1 = iterator;
                    c116512.label = 1;
                    objHasNext = iterator.hasNext(c116512);
                    if (objHasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    $this$consume$iv2 = null;
                    $this$consume$iv3 = $this$consume$iv;
                    try {
                        if (((Boolean) objHasNext).booleanValue()) {
                            ChannelsKt.cancelConsumed($this$consume$iv3, cause$iv);
                            return null;
                        }
                        try {
                            Object single = iterator.next();
                            c116512.L$0 = $this$consume$iv3;
                            c116512.L$1 = single;
                            c116512.label = 2;
                            objHasNext2 = iterator.hasNext(c116512);
                            if (objHasNext2 == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            th = cause$iv;
                            receiveChannel = $this$consume$iv3;
                            obj = single;
                            try {
                                if (((Boolean) objHasNext2).booleanValue()) {
                                    ChannelsKt.cancelConsumed(receiveChannel, th);
                                    return obj;
                                }
                                ChannelsKt.cancelConsumed(receiveChannel, th);
                                return null;
                            } catch (Throwable th2) {
                                e$iv = th2;
                                $this$consume$iv = receiveChannel;
                                Throwable cause$iv2 = e$iv;
                                try {
                                    throw e$iv;
                                } catch (Throwable e$iv2) {
                                    ChannelsKt.cancelConsumed($this$consume$iv, cause$iv2);
                                    throw e$iv2;
                                }
                            }
                        } catch (Throwable th3) {
                            e$iv = th3;
                            $this$consume$iv = $this$consume$iv3;
                            Throwable cause$iv22 = e$iv;
                            throw e$iv;
                        }
                    } catch (Throwable th4) {
                        e$iv = th4;
                        $this$consume$iv = $this$consume$iv3;
                    }
                } catch (Throwable th5) {
                    e$iv = th5;
                    Throwable cause$iv222 = e$iv;
                    throw e$iv;
                }
            case 1:
                ChannelIterator iterator2 = (ChannelIterator) c116512.L$1;
                ReceiveChannel receiveChannel2 = (ReceiveChannel) c116512.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext = $result;
                    $this$consume$iv3 = receiveChannel2;
                    iterator = iterator2;
                    cause$iv = null;
                    $this$consume$iv2 = null;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                } catch (Throwable th6) {
                    $this$consume$iv = receiveChannel2;
                    e$iv = th6;
                    Throwable cause$iv2222 = e$iv;
                    throw e$iv;
                }
                break;
            case 2:
                obj = c116512.L$1;
                receiveChannel = (ReceiveChannel) c116512.L$0;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext2 = $result;
                    th = null;
                    if (((Boolean) objHasNext2).booleanValue()) {
                    }
                } catch (Throwable th7) {
                    e$iv = th7;
                    $this$consume$iv = receiveChannel;
                    Throwable cause$iv22222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$drop$1", m162f = "Deprecated.kt", m163i = {0, 0, 1, 2}, m164l = {194, 199, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION}, m165m = "invokeSuspend", m166n = {"$this$produce", "remaining", "$this$produce", "$this$produce"}, m167s = {"L$0", "I$0", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$drop$1 */
    static final class C11411<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {

        /* renamed from: $n */
        final /* synthetic */ int f228$n;
        final /* synthetic */ ReceiveChannel<E> $this_drop;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11411(int i, ReceiveChannel<? extends E> receiveChannel, Continuation<? super C11411> continuation) {
            super(2, continuation);
            this.f228$n = i;
            this.$this_drop = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11411 c11411 = new C11411(this.f228$n, this.$this_drop, continuation);
            c11411.L$0 = obj;
            return c11411;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C11411) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x007a A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:21:0x007b  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x008b  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x00b5 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00b6  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x00c5  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x00e0  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x007b -> B:22:0x0083). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x00da -> B:30:0x00a5). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11411 c11411;
            ProducerScope $this$produce;
            ChannelIterator<E> it;
            Object $result2;
            ProducerScope $this$produce2;
            ChannelIterator<E> channelIterator;
            int remaining;
            C11411 c114112;
            Object obj;
            ProducerScope $this$produce3;
            ChannelIterator<E> channelIterator2;
            C11411 c114113;
            Object obj2;
            Object $result3;
            Object objHasNext;
            Object $result4 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11411 = this;
                    $this$produce = (ProducerScope) c11411.L$0;
                    boolean z = c11411.f228$n >= 0;
                    int i = c11411.f228$n;
                    if (!z) {
                        throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
                    }
                    int remaining2 = c11411.f228$n;
                    if (remaining2 > 0) {
                        ProducerScope $this$produce4 = $this$produce;
                        int remaining3 = remaining2;
                        ChannelIterator<E> it2 = c11411.$this_drop.iterator();
                        c11411.L$0 = $this$produce4;
                        c11411.L$1 = it2;
                        c11411.I$0 = remaining3;
                        c11411.label = 1;
                        Object objHasNext2 = it2.hasNext(c11411);
                        if (objHasNext2 != $result4) {
                            return $result4;
                        }
                        Object obj3 = $result4;
                        $result2 = $result;
                        $result = objHasNext2;
                        $this$produce2 = $this$produce4;
                        channelIterator = it2;
                        remaining = remaining3;
                        c114112 = c11411;
                        obj = obj3;
                        if (((Boolean) $result).booleanValue()) {
                            channelIterator.next();
                            int remaining4 = remaining - 1;
                            if (remaining4 != 0) {
                                it2 = channelIterator;
                                $this$produce4 = $this$produce2;
                                C11411 c114114 = c114112;
                                remaining3 = remaining4;
                                $result = $result2;
                                $result4 = obj;
                                c11411 = c114114;
                                c11411.L$0 = $this$produce4;
                                c11411.L$1 = it2;
                                c11411.I$0 = remaining3;
                                c11411.label = 1;
                                Object objHasNext22 = it2.hasNext(c11411);
                                if (objHasNext22 != $result4) {
                                }
                            }
                        }
                        $result = $result2;
                        $result4 = obj;
                        c11411 = c114112;
                        $this$produce = $this$produce2;
                    }
                    it = c11411.$this_drop.iterator();
                    c11411.L$0 = $this$produce;
                    c11411.L$1 = it;
                    c11411.label = 2;
                    objHasNext = it.hasNext(c11411);
                    if (objHasNext != $result4) {
                        return $result4;
                    }
                    Object obj4 = $result4;
                    $result3 = $result;
                    $result = objHasNext;
                    $this$produce3 = $this$produce;
                    channelIterator2 = it;
                    c114113 = c11411;
                    obj2 = obj4;
                    if (((Boolean) $result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    c114113.L$0 = $this$produce3;
                    c114113.L$1 = channelIterator2;
                    c114113.label = 3;
                    Object e = $this$produce3.send(channelIterator2.next(), c114113);
                    if (e == obj2) {
                        return obj2;
                    }
                    $result = $result3;
                    $result4 = obj2;
                    c11411 = c114113;
                    it = channelIterator2;
                    $this$produce = $this$produce3;
                    c11411.L$0 = $this$produce;
                    c11411.L$1 = it;
                    c11411.label = 2;
                    objHasNext = it.hasNext(c11411);
                    if (objHasNext != $result4) {
                    }
                case 1:
                    int remaining5 = this.I$0;
                    ChannelIterator<E> channelIterator3 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce5 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce2 = $this$produce5;
                    channelIterator = channelIterator3;
                    remaining = remaining5;
                    c114112 = this;
                    obj = $result4;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    $result = $result2;
                    $result4 = obj;
                    c11411 = c114112;
                    $this$produce = $this$produce2;
                    it = c11411.$this_drop.iterator();
                    c11411.L$0 = $this$produce;
                    c11411.L$1 = it;
                    c11411.label = 2;
                    objHasNext = it.hasNext(c11411);
                    if (objHasNext != $result4) {
                    }
                    break;
                case 2:
                    ChannelIterator<E> channelIterator4 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce6 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce3 = $this$produce6;
                    channelIterator2 = channelIterator4;
                    c114113 = this;
                    obj2 = $result4;
                    $result3 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 3:
                    c11411 = this;
                    it = (ChannelIterator) c11411.L$1;
                    $this$produce = (ProducerScope) c11411.L$0;
                    ResultKt.throwOnFailure($result);
                    c11411.L$0 = $this$produce;
                    c11411.L$1 = it;
                    c11411.label = 2;
                    objHasNext = it.hasNext(c11411);
                    if (objHasNext != $result4) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static /* synthetic */ ReceiveChannel drop$default(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return drop(receiveChannel, i, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel drop(ReceiveChannel $this$drop, int n, CoroutineContext context) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes($this$drop), new C11411(n, $this$drop, null));
    }

    public static /* synthetic */ ReceiveChannel dropWhile$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return dropWhile(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$dropWhile$1", m162f = "Deprecated.kt", m163i = {0, 1, 1, 2, 3, 4}, m164l = {211, 212, 213, 217, 218}, m165m = "invokeSuspend", m166n = {"$this$produce", "$this$produce", "e", "$this$produce", "$this$produce", "$this$produce"}, m167s = {"L$0", "L$0", "L$2", "L$0", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$dropWhile$1 */
    static final class C11421<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_dropWhile;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11421(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C11421> continuation) {
            super(2, continuation);
            this.$this_dropWhile = receiveChannel;
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11421 c11421 = new C11421(this.$this_dropWhile, this.$predicate, continuation);
            c11421.L$0 = obj;
            return c11421;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C11421) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0088 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:15:0x0089  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0097  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00b9  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00d5  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x00f1 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00fd  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x0114  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x00ad -> B:22:0x00b1). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x0112 -> B:31:0x00e1). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            ChannelIterator<E> it;
            ProducerScope $this$produce;
            C11421 c11421;
            Object obj;
            Object $result2;
            E e;
            ChannelIterator<E> channelIterator;
            C11421 c114212;
            ProducerScope $this$produce2;
            ChannelIterator<E> it2;
            ChannelIterator<E> channelIterator2;
            Object objHasNext;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    C11421 c114213 = this;
                    ProducerScope $this$produce3 = (ProducerScope) c114213.L$0;
                    it = c114213.$this_dropWhile.iterator();
                    c114213.L$0 = $this$produce3;
                    c114213.L$1 = it;
                    c114213.L$2 = null;
                    c114213.label = 1;
                    Object objHasNext2 = it.hasNext(c114213);
                    if (objHasNext2 != $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext2;
                    $this$produce = $this$produce3;
                    c11421 = c114213;
                    obj = obj2;
                    if (((Boolean) $result).booleanValue()) {
                        E next = it.next();
                        Function2<E, Continuation<? super Boolean>, Object> function2 = c11421.$predicate;
                        c11421.L$0 = $this$produce;
                        c11421.L$1 = it;
                        c11421.L$2 = next;
                        c11421.label = 2;
                        Object objInvoke = function2.invoke(next, c11421);
                        if (objInvoke == obj) {
                            return obj;
                        }
                        ChannelIterator<E> channelIterator3 = it;
                        e = next;
                        $result = objInvoke;
                        channelIterator = channelIterator3;
                        if (((Boolean) $result).booleanValue()) {
                            c11421.L$0 = $this$produce;
                            c11421.L$1 = null;
                            c11421.L$2 = null;
                            c11421.label = 3;
                            if ($this$produce.send(e, c11421) == obj) {
                                return obj;
                            }
                            $result = $result2;
                            $result3 = obj;
                            c114212 = c11421;
                            $this$produce2 = $this$produce;
                            c11421 = c114212;
                            $this$produce = $this$produce2;
                            obj = $result3;
                        } else {
                            $result = $result2;
                            $result3 = obj;
                            c114213 = c11421;
                            $this$produce3 = $this$produce;
                            it = channelIterator;
                            c114213.L$0 = $this$produce3;
                            c114213.L$1 = it;
                            c114213.L$2 = null;
                            c114213.label = 1;
                            Object objHasNext22 = it.hasNext(c114213);
                            if (objHasNext22 != $result3) {
                            }
                        }
                    }
                    it2 = c11421.$this_dropWhile.iterator();
                    c11421.L$0 = $this$produce;
                    c11421.L$1 = it2;
                    c11421.label = 4;
                    objHasNext = it2.hasNext(c11421);
                    if (objHasNext == obj) {
                        return obj;
                    }
                    channelIterator2 = it2;
                    $result = objHasNext;
                    if (((Boolean) $result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    c11421.L$0 = $this$produce;
                    c11421.L$1 = channelIterator2;
                    c11421.label = 5;
                    Object e2 = $this$produce.send(channelIterator2.next(), c11421);
                    if (e2 == obj) {
                        return obj;
                    }
                    it2 = channelIterator2;
                    c11421.L$0 = $this$produce;
                    c11421.L$1 = it2;
                    c11421.label = 4;
                    objHasNext = it2.hasNext(c11421);
                    if (objHasNext == obj) {
                    }
                case 1:
                    ChannelIterator<E> channelIterator4 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce4 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce = $this$produce4;
                    it = channelIterator4;
                    c11421 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    it2 = c11421.$this_dropWhile.iterator();
                    c11421.L$0 = $this$produce;
                    c11421.L$1 = it2;
                    c11421.label = 4;
                    objHasNext = it2.hasNext(c11421);
                    if (objHasNext == obj) {
                    }
                    break;
                case 2:
                    Object obj3 = this.L$2;
                    ChannelIterator<E> channelIterator5 = (ChannelIterator) this.L$1;
                    $this$produce = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    channelIterator = channelIterator5;
                    e = obj3;
                    c11421 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 3:
                    c114212 = this;
                    $this$produce2 = (ProducerScope) c114212.L$0;
                    ResultKt.throwOnFailure($result);
                    c11421 = c114212;
                    $this$produce = $this$produce2;
                    obj = $result3;
                    it2 = c11421.$this_dropWhile.iterator();
                    c11421.L$0 = $this$produce;
                    c11421.L$1 = it2;
                    c11421.label = 4;
                    objHasNext = it2.hasNext(c11421);
                    if (objHasNext == obj) {
                    }
                    break;
                case 4:
                    channelIterator2 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce5 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce = $this$produce5;
                    c11421 = this;
                    obj = $result3;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 5:
                    ChannelIterator<E> channelIterator6 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce6 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce = $this$produce6;
                    c11421 = this;
                    obj = $result3;
                    it2 = channelIterator6;
                    c11421.L$0 = $this$produce;
                    c11421.L$1 = it2;
                    c11421.label = 4;
                    objHasNext = it2.hasNext(c11421);
                    if (objHasNext == obj) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel dropWhile(ReceiveChannel $this$dropWhile, CoroutineContext context, Function2 predicate) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes($this$dropWhile), new C11421($this$dropWhile, predicate, null));
    }

    public static /* synthetic */ ReceiveChannel filter$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.filter(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filter$1", m162f = "Deprecated.kt", m163i = {0, 1, 1, 2}, m164l = {228, 229, 229}, m165m = "invokeSuspend", m166n = {"$this$produce", "$this$produce", "e", "$this$produce"}, m167s = {"L$0", "L$0", "L$2", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filter$1 */
    static final class C11451<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_filter;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11451(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C11451> continuation) {
            super(2, continuation);
            this.$this_filter = receiveChannel;
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11451 c11451 = new C11451(this.$this_filter, this.$predicate, continuation);
            c11451.L$0 = obj;
            return c11451;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C11451) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:12:0x006b A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:13:0x006c  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x007b  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x009e  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00b7  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00bd  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x00b1 -> B:10:0x0059). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x00b7 -> B:10:0x0059). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11451 c11451;
            ProducerScope $this$produce;
            ChannelIterator<E> it;
            ProducerScope $this$produce2;
            ChannelIterator<E> channelIterator;
            C11451 c114512;
            Object obj;
            Object $result2;
            ProducerScope $this$produce3;
            ChannelIterator<E> channelIterator2;
            E e;
            Object objHasNext;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11451 = this;
                    ProducerScope $this$produce4 = (ProducerScope) c11451.L$0;
                    $this$produce = $this$produce4;
                    it = c11451.$this_filter.iterator();
                    c11451.L$0 = $this$produce;
                    c11451.L$1 = it;
                    c11451.L$2 = null;
                    c11451.label = 1;
                    objHasNext = it.hasNext(c11451);
                    if (objHasNext != $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext;
                    $this$produce2 = $this$produce;
                    channelIterator = it;
                    c114512 = c11451;
                    obj = obj2;
                    if (((Boolean) $result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    E next = channelIterator.next();
                    Function2<E, Continuation<? super Boolean>, Object> function2 = c114512.$predicate;
                    c114512.L$0 = $this$produce2;
                    c114512.L$1 = channelIterator;
                    c114512.L$2 = next;
                    c114512.label = 2;
                    Object objInvoke = function2.invoke(next, c114512);
                    if (objInvoke == obj) {
                        return obj;
                    }
                    ChannelIterator<E> channelIterator3 = channelIterator;
                    e = next;
                    $result = objInvoke;
                    $this$produce3 = $this$produce2;
                    channelIterator2 = channelIterator3;
                    if (((Boolean) $result).booleanValue()) {
                        $result = $result2;
                        $result3 = obj;
                        c11451 = c114512;
                        it = channelIterator2;
                        $this$produce = $this$produce3;
                    } else {
                        c114512.L$0 = $this$produce3;
                        c114512.L$1 = channelIterator2;
                        c114512.L$2 = null;
                        c114512.label = 3;
                        if ($this$produce3.send(e, c114512) == obj) {
                            return obj;
                        }
                        $result = $result2;
                        $result3 = obj;
                        c11451 = c114512;
                        it = channelIterator2;
                        $this$produce = $this$produce3;
                    }
                    c11451.L$0 = $this$produce;
                    c11451.L$1 = it;
                    c11451.L$2 = null;
                    c11451.label = 1;
                    objHasNext = it.hasNext(c11451);
                    if (objHasNext != $result3) {
                    }
                case 1:
                    ChannelIterator<E> channelIterator4 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce5 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce2 = $this$produce5;
                    channelIterator = channelIterator4;
                    c114512 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 2:
                    Object obj3 = this.L$2;
                    ChannelIterator<E> channelIterator5 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce6 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce3 = $this$produce6;
                    channelIterator2 = channelIterator5;
                    e = obj3;
                    c114512 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    c11451.L$0 = $this$produce;
                    c11451.L$1 = it;
                    c11451.L$2 = null;
                    c11451.label = 1;
                    objHasNext = it.hasNext(c11451);
                    if (objHasNext != $result3) {
                    }
                    break;
                case 3:
                    c11451 = this;
                    it = (ChannelIterator) c11451.L$1;
                    $this$produce = (ProducerScope) c11451.L$0;
                    ResultKt.throwOnFailure($result);
                    c11451.L$0 = $this$produce;
                    c11451.L$1 = it;
                    c11451.L$2 = null;
                    c11451.label = 1;
                    objHasNext = it.hasNext(c11451);
                    if (objHasNext != $result3) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <E> ReceiveChannel<E> filter(ReceiveChannel<? extends E> receiveChannel, CoroutineContext context, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes(receiveChannel), new C11451(receiveChannel, function2, null));
    }

    public static /* synthetic */ ReceiveChannel filterIndexed$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return filterIndexed(receiveChannel, coroutineContext, function3);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterIndexed$1", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1, 1, 2, 2}, m164l = {241, 242, 242}, m165m = "invokeSuspend", m166n = {"$this$produce", "index", "$this$produce", "e", "index", "$this$produce", "index"}, m167s = {"L$0", "I$0", "L$0", "L$2", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterIndexed$1 */
    static final class C11461<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function3<Integer, E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_filterIndexed;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11461(ReceiveChannel<? extends E> receiveChannel, Function3<? super Integer, ? super E, ? super Continuation<? super Boolean>, ? extends Object> function3, Continuation<? super C11461> continuation) {
            super(2, continuation);
            this.$this_filterIndexed = receiveChannel;
            this.$predicate = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11461 c11461 = new C11461(this.$this_filterIndexed, this.$predicate, continuation);
            c11461.L$0 = obj;
            return c11461;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C11461) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0074 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0075  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0085  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x00ae  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00ca  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00d1  */
        /* JADX WARN: Type inference failed for: r6v10 */
        /* JADX WARN: Type inference failed for: r6v3, types: [java.lang.Object, kotlinx.coroutines.channels.ProducerScope] */
        /* JADX WARN: Type inference failed for: r6v9 */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x00c3 -> B:10:0x0060). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x00ca -> B:10:0x0060). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) {
            C11461<E> c11461;
            ProducerScope producerScope;
            int i;
            ChannelIterator<E> it;
            ProducerScope producerScope2;
            ChannelIterator<E> channelIterator;
            int i2;
            C11461<E> c114612;
            Object obj2;
            Object obj3;
            Object obj4;
            int i3;
            ?? r6;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    c11461 = this;
                    producerScope = (ProducerScope) c11461.L$0;
                    i = 0;
                    it = c11461.$this_filterIndexed.iterator();
                    c11461.L$0 = producerScope;
                    c11461.L$1 = it;
                    c11461.L$2 = null;
                    c11461.I$0 = i;
                    c11461.label = 1;
                    objHasNext = it.hasNext(c11461);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Object obj5 = coroutine_suspended;
                    obj3 = obj;
                    obj = objHasNext;
                    producerScope2 = producerScope;
                    channelIterator = it;
                    i2 = i;
                    c114612 = c11461;
                    obj2 = obj5;
                    if (((Boolean) obj).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    E next = channelIterator.next();
                    Function3<Integer, E, Continuation<? super Boolean>, Object> function3 = c114612.$predicate;
                    i3 = i2 + 1;
                    Integer numBoxInt = Boxing.boxInt(i2);
                    c114612.L$0 = producerScope2;
                    c114612.L$1 = channelIterator;
                    c114612.L$2 = next;
                    c114612.I$0 = i3;
                    c114612.label = 2;
                    Object objInvoke = function3.invoke(numBoxInt, next, c114612);
                    if (objInvoke == obj2) {
                        return obj2;
                    }
                    obj4 = next;
                    obj = objInvoke;
                    r6 = producerScope2;
                    if (((Boolean) obj).booleanValue()) {
                        obj = obj3;
                        coroutine_suspended = obj2;
                        c11461 = c114612;
                        it = channelIterator;
                        producerScope = r6;
                        i = i3;
                    } else {
                        c114612.L$0 = r6;
                        c114612.L$1 = channelIterator;
                        c114612.L$2 = null;
                        c114612.I$0 = i3;
                        c114612.label = 3;
                        if (r6.send(obj4, c114612) == obj2) {
                            return obj2;
                        }
                        obj = obj3;
                        coroutine_suspended = obj2;
                        c11461 = c114612;
                        it = channelIterator;
                        producerScope = r6;
                        i = i3;
                    }
                    c11461.L$0 = producerScope;
                    c11461.L$1 = it;
                    c11461.L$2 = null;
                    c11461.I$0 = i;
                    c11461.label = 1;
                    objHasNext = it.hasNext(c11461);
                    if (objHasNext != coroutine_suspended) {
                    }
                case 1:
                    int i4 = this.I$0;
                    ChannelIterator<E> channelIterator2 = (ChannelIterator) this.L$1;
                    ProducerScope producerScope3 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure(obj);
                    producerScope2 = producerScope3;
                    channelIterator = channelIterator2;
                    i2 = i4;
                    c114612 = this;
                    obj2 = coroutine_suspended;
                    obj3 = obj;
                    if (((Boolean) obj).booleanValue()) {
                    }
                    break;
                case 2:
                    int i5 = this.I$0;
                    obj4 = this.L$2;
                    channelIterator = (ChannelIterator) this.L$1;
                    ProducerScope producerScope4 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure(obj);
                    i3 = i5;
                    c114612 = this;
                    obj2 = coroutine_suspended;
                    obj3 = obj;
                    r6 = producerScope4;
                    if (((Boolean) obj).booleanValue()) {
                    }
                    c11461.L$0 = producerScope;
                    c11461.L$1 = it;
                    c11461.L$2 = null;
                    c11461.I$0 = i;
                    c11461.label = 1;
                    objHasNext = it.hasNext(c11461);
                    if (objHasNext != coroutine_suspended) {
                    }
                    break;
                case 3:
                    c11461 = this;
                    i = c11461.I$0;
                    it = (ChannelIterator) c11461.L$1;
                    producerScope = (ProducerScope) c11461.L$0;
                    ResultKt.throwOnFailure(obj);
                    c11461.L$0 = producerScope;
                    c11461.L$1 = it;
                    c11461.L$2 = null;
                    c11461.I$0 = i;
                    c11461.label = 1;
                    objHasNext = it.hasNext(c11461);
                    if (objHasNext != coroutine_suspended) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel filterIndexed(ReceiveChannel $this$filterIndexed, CoroutineContext context, Function3 predicate) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes($this$filterIndexed), new C11461($this$filterIndexed, predicate, null));
    }

    public static /* synthetic */ ReceiveChannel filterNot$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return filterNot(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "it"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNot$1", m162f = "Deprecated.kt", m163i = {}, m164l = {252}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNot$1 */
    static final class C11471<E> extends SuspendLambda implements Function2<E, Continuation<? super Boolean>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11471(Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C11471> continuation) {
            super(2, continuation);
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11471 c11471 = new C11471(this.$predicate, continuation);
            c11471.L$0 = obj;
            return c11471;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Continuation<? super Boolean> continuation) {
            return invoke2((C11471<E>) obj, continuation);
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(E e, Continuation<? super Boolean> continuation) {
            return ((C11471) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    Object obj2 = this.L$0;
                    Function2<E, Continuation<? super Boolean>, Object> function2 = this.$predicate;
                    this.label = 1;
                    Object objInvoke = function2.invoke(obj2, this);
                    if (objInvoke != coroutine_suspended) {
                        obj = objInvoke;
                        break;
                    } else {
                        return coroutine_suspended;
                    }
                case 1:
                    ResultKt.throwOnFailure(obj);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Boxing.boxBoolean(!((Boolean) obj).booleanValue());
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel filterNot(ReceiveChannel $this$filterNot, CoroutineContext context, Function2 predicate) {
        return ChannelsKt.filter($this$filterNot, context, new C11471(predicate, null));
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0010\u0000\u001a\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u0001H\u0002H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "", "it"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNull$1", m162f = "Deprecated.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNull$1 */
    static final class C11481<E> extends SuspendLambda implements Function2<E, Continuation<? super Boolean>, Object> {
        /* synthetic */ Object L$0;
        int label;

        C11481(Continuation<? super C11481> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11481 c11481 = new C11481(continuation);
            c11481.L$0 = obj;
            return c11481;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Continuation<? super Boolean> continuation) {
            return invoke2((C11481<E>) obj, continuation);
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(E e, Continuation<? super Boolean> continuation) {
            return ((C11481) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    Object it = this.L$0;
                    return Boxing.boxBoolean(it != null);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <E> ReceiveChannel<E> filterNotNull(ReceiveChannel<? extends E> receiveChannel) {
        ReceiveChannel<E> receiveChannelFilter$default = filter$default(receiveChannel, null, new C11481(null), 1, null);
        Intrinsics.checkNotNull(receiveChannelFilter$default, "null cannot be cast to non-null type kotlinx.coroutines.channels.ReceiveChannel<E of kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.filterNotNull>");
        return receiveChannelFilter$default;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0070 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0082 A[Catch: all -> 0x009f, TryCatch #3 {all -> 0x009f, blocks: (B:24:0x007a, B:26:0x0082, B:28:0x0089, B:30:0x0095), top: B:48:0x007a }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0095 A[Catch: all -> 0x009f, TRY_LEAVE, TryCatch #3 {all -> 0x009f, blocks: (B:24:0x007a, B:26:0x0082, B:28:0x0089, B:30:0x0095), top: B:48:0x007a }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0071 -> B:48:0x007a). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object filterNotNullTo(ReceiveChannel $this$consumeEach$iv, Collection destination, Continuation continuation) {
        C11491 c11491;
        ReceiveChannel $this$consume$iv$iv;
        Throwable cause$iv$iv;
        Object $result;
        Collection destination2;
        ReceiveChannel $this$consume$iv$iv2;
        Throwable cause$iv$iv2;
        ChannelIterator channelIterator;
        int i;
        Object obj;
        if (continuation instanceof C11491) {
            c11491 = (C11491) continuation;
            if ((c11491.label & Integer.MIN_VALUE) != 0) {
                c11491.label -= Integer.MIN_VALUE;
            } else {
                c11491 = new C11491(continuation);
            }
        }
        C11491 c114912 = c11491;
        Object $result2 = c114912.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c114912.label) {
            case 0:
                ResultKt.throwOnFailure($result2);
                $this$consume$iv$iv = $this$consumeEach$iv;
                Throwable cause$iv$iv3 = null;
                try {
                    ChannelIterator it = $this$consume$iv$iv.iterator();
                    int $i$f$consumeEach = 0;
                    Collection destination3 = destination;
                    c114912.L$0 = destination3;
                    c114912.L$1 = $this$consume$iv$iv;
                    c114912.L$2 = it;
                    c114912.label = 1;
                    Object objHasNext = it.hasNext(c114912);
                    if (objHasNext != $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result = $result2;
                    $result2 = objHasNext;
                    destination2 = destination3;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv2 = cause$iv$iv3;
                    channelIterator = it;
                    i = $i$f$consumeEach;
                    obj = obj2;
                    try {
                        if (((Boolean) $result2).booleanValue()) {
                            Object it2 = channelIterator.next();
                            if (it2 != null) {
                                destination2.add(it2);
                            }
                            $result2 = $result;
                            $result3 = obj;
                            $i$f$consumeEach = i;
                            it = channelIterator;
                            cause$iv$iv3 = cause$iv$iv;
                            $this$consume$iv$iv = $this$consume$iv$iv;
                            destination3 = destination2;
                            c114912.L$0 = destination3;
                            c114912.L$1 = $this$consume$iv$iv;
                            c114912.L$2 = it;
                            c114912.label = 1;
                            Object objHasNext2 = it.hasNext(c114912);
                            if (objHasNext2 != $result3) {
                            }
                        } else {
                            Unit unit = Unit.INSTANCE;
                            return destination2;
                        }
                    } catch (Throwable th) {
                        $this$consume$iv$iv = $this$consume$iv$iv;
                        e$iv$iv = th;
                        cause$iv$iv = e$iv$iv;
                        try {
                            throw e$iv$iv;
                        } finally {
                            ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv);
                        }
                    }
                } catch (Throwable th2) {
                    e$iv$iv = th2;
                    cause$iv$iv = e$iv$iv;
                    throw e$iv$iv;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c114912.L$2;
                $this$consume$iv$iv = (ReceiveChannel) c114912.L$1;
                Collection destination4 = (Collection) c114912.L$0;
                try {
                    ResultKt.throwOnFailure($result2);
                    destination2 = destination4;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv2 = null;
                    channelIterator = channelIterator2;
                    i = 0;
                    obj = $result3;
                    $result = $result2;
                    if (((Boolean) $result2).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv$iv = th3;
                    cause$iv$iv = e$iv$iv;
                    throw e$iv$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0084 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0093 A[Catch: all -> 0x00c7, TryCatch #1 {all -> 0x00c7, blocks: (B:27:0x008b, B:29:0x0093, B:31:0x009a, B:38:0x00bd), top: B:54:0x008b }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00bd A[Catch: all -> 0x00c7, TRY_LEAVE, TryCatch #1 {all -> 0x00c7, blocks: (B:27:0x008b, B:29:0x0093, B:31:0x009a, B:38:0x00bd), top: B:54:0x008b }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x00aa -> B:35:0x00af). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:36:0x00b8 -> B:37:0x00bc). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object filterNotNullTo(ReceiveChannel $this$consumeEach$iv, SendChannel destination, Continuation continuation) {
        C11503 c11503;
        ReceiveChannel $this$consume$iv$iv;
        Throwable cause$iv$iv;
        int i;
        ChannelIterator it;
        Continuation $continuation;
        Object obj;
        Object $result;
        C11503 c115032;
        Continuation continuation2;
        ChannelIterator channelIterator;
        SendChannel destination2;
        int i2;
        ChannelIterator channelIterator2;
        Object objHasNext;
        if (continuation instanceof C11503) {
            c11503 = (C11503) continuation;
            if ((c11503.label & Integer.MIN_VALUE) != 0) {
                c11503.label -= Integer.MIN_VALUE;
            } else {
                c11503 = new C11503(continuation);
            }
        }
        C11503 c115033 = c11503;
        Object $result2 = c115033.result;
        Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c115033.label) {
                case 0:
                    ResultKt.throwOnFailure($result2);
                    $this$consume$iv$iv = $this$consumeEach$iv;
                    cause$iv$iv = null;
                    i = 0;
                    try {
                        it = $this$consume$iv$iv.iterator();
                        $continuation = null;
                        obj = $result3;
                        $result = $result2;
                        c115032 = c115033;
                    } catch (Throwable th) {
                        e$iv$iv = th;
                        Throwable cause$iv$iv2 = e$iv$iv;
                        try {
                            throw e$iv$iv;
                        } finally {
                            ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv2);
                        }
                    }
                    try {
                        c115032.L$0 = destination;
                        c115032.L$1 = $this$consume$iv$iv;
                        c115032.L$2 = it;
                        c115032.label = 1;
                        objHasNext = it.hasNext(c115032);
                        if (objHasNext != obj) {
                            return obj;
                        }
                        ChannelIterator channelIterator3 = it;
                        destination2 = destination;
                        continuation2 = $continuation;
                        c115033 = c115032;
                        $result2 = objHasNext;
                        channelIterator = channelIterator3;
                        try {
                            if (!((Boolean) $result2).booleanValue()) {
                                Unit unit = Unit.INSTANCE;
                                return destination2;
                            }
                            Object it2 = channelIterator.next();
                            if (it2 != null) {
                                c115033.L$0 = destination2;
                                c115033.L$1 = $this$consume$iv$iv;
                                c115033.L$2 = channelIterator;
                                c115033.label = 2;
                                if (destination2.send(it2, c115033) == obj) {
                                    return obj;
                                }
                                $result2 = $result;
                                $result3 = obj;
                                i2 = i;
                                channelIterator2 = channelIterator;
                                C11503 c115034 = c115033;
                                $continuation = continuation2;
                                destination = destination2;
                                it = channelIterator2;
                                i = i2;
                                obj = $result3;
                                $result = $result2;
                                c115032 = c115034;
                                c115032.L$0 = destination;
                                c115032.L$1 = $this$consume$iv$iv;
                                c115032.L$2 = it;
                                c115032.label = 1;
                                objHasNext = it.hasNext(c115032);
                                if (objHasNext != obj) {
                                }
                            } else {
                                c115032 = c115033;
                                $continuation = continuation2;
                                destination = destination2;
                                it = channelIterator;
                                c115032.L$0 = destination;
                                c115032.L$1 = $this$consume$iv$iv;
                                c115032.L$2 = it;
                                c115032.label = 1;
                                objHasNext = it.hasNext(c115032);
                                if (objHasNext != obj) {
                                }
                            }
                        } catch (Throwable th2) {
                            e$iv$iv = th2;
                            Throwable cause$iv$iv22 = e$iv$iv;
                            throw e$iv$iv;
                        }
                    } catch (Throwable th3) {
                        e$iv$iv = th3;
                        Throwable cause$iv$iv222 = e$iv$iv;
                        throw e$iv$iv;
                    }
                case 1:
                    continuation2 = null;
                    channelIterator = (ChannelIterator) c115033.L$2;
                    cause$iv$iv = null;
                    ReceiveChannel $this$consume$iv$iv2 = (ReceiveChannel) c115033.L$1;
                    $this$consume$iv$iv = $this$consume$iv$iv2;
                    SendChannel destination3 = (SendChannel) c115033.L$0;
                    ResultKt.throwOnFailure($result2);
                    destination2 = destination3;
                    i = 0;
                    obj = $result3;
                    $result = $result2;
                    if (!((Boolean) $result2).booleanValue()) {
                    }
                    break;
                case 2:
                    continuation2 = null;
                    i2 = 0;
                    channelIterator2 = (ChannelIterator) c115033.L$2;
                    cause$iv$iv = null;
                    $this$consume$iv$iv = (ReceiveChannel) c115033.L$1;
                    destination2 = (SendChannel) c115033.L$0;
                    ResultKt.throwOnFailure($result2);
                    C11503 c1150342 = c115033;
                    $continuation = continuation2;
                    destination = destination2;
                    it = channelIterator2;
                    i = i2;
                    obj = $result3;
                    $result = $result2;
                    c115032 = c1150342;
                    c115032.L$0 = destination;
                    c115032.L$1 = $this$consume$iv$iv;
                    c115032.L$2 = it;
                    c115032.label = 1;
                    objHasNext = it.hasNext(c115032);
                    if (objHasNext != obj) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th4) {
            e$iv$iv = th4;
        }
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$take$1", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1}, m164l = {284, 285}, m165m = "invokeSuspend", m166n = {"$this$produce", "remaining", "$this$produce", "remaining"}, m167s = {"L$0", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$take$1 */
    static final class C11661<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {

        /* renamed from: $n */
        final /* synthetic */ int f229$n;
        final /* synthetic */ ReceiveChannel<E> $this_take;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11661(int i, ReceiveChannel<? extends E> receiveChannel, Continuation<? super C11661> continuation) {
            super(2, continuation);
            this.f229$n = i;
            this.$this_take = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11661 c11661 = new C11661(this.f229$n, this.$this_take, continuation);
            c11661.L$0 = obj;
            return c11661;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C11661) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x006a A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:22:0x006b  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0079  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x009a  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x009d  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00a2  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x0090 -> B:29:0x0096). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11661 c11661;
            int remaining;
            ChannelIterator<E> it;
            ProducerScope $this$produce;
            C11661 c116612;
            Object obj;
            Object $result2;
            int remaining2;
            ChannelIterator<E> channelIterator;
            ProducerScope $this$produce2;
            int remaining3;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11661 = this;
                    ProducerScope $this$produce3 = (ProducerScope) c11661.L$0;
                    if (c11661.f229$n == 0) {
                        return Unit.INSTANCE;
                    }
                    boolean z = c11661.f229$n >= 0;
                    int i = c11661.f229$n;
                    if (!z) {
                        throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
                    }
                    remaining = c11661.f229$n;
                    it = c11661.$this_take.iterator();
                    c11661.L$0 = $this$produce3;
                    c11661.L$1 = it;
                    c11661.I$0 = remaining;
                    c11661.label = 1;
                    Object objHasNext = it.hasNext(c11661);
                    if (objHasNext != $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext;
                    $this$produce = $this$produce3;
                    c116612 = c11661;
                    obj = obj2;
                    if (!((Boolean) $result).booleanValue()) {
                        c116612.L$0 = $this$produce;
                        c116612.L$1 = it;
                        c116612.I$0 = remaining;
                        c116612.label = 2;
                        Object e = $this$produce.send(it.next(), c116612);
                        if (e == obj) {
                            return obj;
                        }
                        $result = $result2;
                        $result3 = obj;
                        c11661 = c116612;
                        remaining2 = remaining;
                        channelIterator = it;
                        $this$produce2 = $this$produce;
                        remaining3 = remaining2 - 1;
                        if (remaining3 == 0) {
                            ChannelIterator<E> channelIterator2 = channelIterator;
                            remaining = remaining3;
                            $this$produce3 = $this$produce2;
                            it = channelIterator2;
                            c11661.L$0 = $this$produce3;
                            c11661.L$1 = it;
                            c11661.I$0 = remaining;
                            c11661.label = 1;
                            Object objHasNext2 = it.hasNext(c11661);
                            if (objHasNext2 != $result3) {
                            }
                        } else {
                            return Unit.INSTANCE;
                        }
                    } else {
                        return Unit.INSTANCE;
                    }
                case 1:
                    int remaining4 = this.I$0;
                    ChannelIterator<E> channelIterator3 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce4 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce = $this$produce4;
                    it = channelIterator3;
                    remaining = remaining4;
                    c116612 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (!((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 2:
                    c11661 = this;
                    remaining2 = c11661.I$0;
                    channelIterator = (ChannelIterator) c11661.L$1;
                    $this$produce2 = (ProducerScope) c11661.L$0;
                    ResultKt.throwOnFailure($result);
                    remaining3 = remaining2 - 1;
                    if (remaining3 == 0) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static /* synthetic */ ReceiveChannel take$default(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return take(receiveChannel, i, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel take(ReceiveChannel $this$take, int n, CoroutineContext context) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes($this$take), new C11661(n, $this$take, null));
    }

    public static /* synthetic */ ReceiveChannel takeWhile$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return takeWhile(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$takeWhile$1", m162f = "Deprecated.kt", m163i = {0, 1, 1, 2}, m164l = {299, MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION, 301}, m165m = "invokeSuspend", m166n = {"$this$produce", "$this$produce", "e", "$this$produce"}, m167s = {"L$0", "L$0", "L$2", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$takeWhile$1 */
    static final class C11671<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_takeWhile;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11671(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C11671> continuation) {
            super(2, continuation);
            this.$this_takeWhile = receiveChannel;
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11671 c11671 = new C11671(this.$this_takeWhile, this.$predicate, continuation);
            c11671.L$0 = obj;
            return c11671;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C11671) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0068 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0069  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0078  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x009b  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x009e  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00b8  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x00b2 -> B:10:0x0058). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11671 c11671;
            ProducerScope $this$produce;
            ChannelIterator<E> it;
            ProducerScope $this$produce2;
            ChannelIterator<E> channelIterator;
            C11671 c116712;
            Object obj;
            Object $result2;
            ProducerScope $this$produce3;
            ChannelIterator<E> channelIterator2;
            E e;
            Object objHasNext;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11671 = this;
                    ProducerScope $this$produce4 = (ProducerScope) c11671.L$0;
                    $this$produce = $this$produce4;
                    it = c11671.$this_takeWhile.iterator();
                    c11671.L$0 = $this$produce;
                    c11671.L$1 = it;
                    c11671.label = 1;
                    objHasNext = it.hasNext(c11671);
                    if (objHasNext == $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext;
                    $this$produce2 = $this$produce;
                    channelIterator = it;
                    c116712 = c11671;
                    obj = obj2;
                    if (((Boolean) $result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    E next = channelIterator.next();
                    Function2<E, Continuation<? super Boolean>, Object> function2 = c116712.$predicate;
                    c116712.L$0 = $this$produce2;
                    c116712.L$1 = channelIterator;
                    c116712.L$2 = next;
                    c116712.label = 2;
                    Object objInvoke = function2.invoke(next, c116712);
                    if (objInvoke == obj) {
                        return obj;
                    }
                    ChannelIterator<E> channelIterator3 = channelIterator;
                    e = next;
                    $result = objInvoke;
                    $this$produce3 = $this$produce2;
                    channelIterator2 = channelIterator3;
                    if (((Boolean) $result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    c116712.L$0 = $this$produce3;
                    c116712.L$1 = channelIterator2;
                    c116712.L$2 = null;
                    c116712.label = 3;
                    if ($this$produce3.send(e, c116712) == obj) {
                        return obj;
                    }
                    $result = $result2;
                    $result3 = obj;
                    c11671 = c116712;
                    it = channelIterator2;
                    $this$produce = $this$produce3;
                    c11671.L$0 = $this$produce;
                    c11671.L$1 = it;
                    c11671.label = 1;
                    objHasNext = it.hasNext(c11671);
                    if (objHasNext == $result3) {
                    }
                case 1:
                    ChannelIterator<E> channelIterator4 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce5 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce2 = $this$produce5;
                    channelIterator = channelIterator4;
                    c116712 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 2:
                    Object obj3 = this.L$2;
                    ChannelIterator<E> channelIterator5 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce6 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce3 = $this$produce6;
                    channelIterator2 = channelIterator5;
                    e = obj3;
                    c116712 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 3:
                    c11671 = this;
                    it = (ChannelIterator) c11671.L$1;
                    $this$produce = (ProducerScope) c11671.L$0;
                    ResultKt.throwOnFailure($result);
                    c11671.L$0 = $this$produce;
                    c11671.L$1 = it;
                    c11671.label = 1;
                    objHasNext = it.hasNext(c11671);
                    if (objHasNext == $result3) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel takeWhile(ReceiveChannel $this$takeWhile, CoroutineContext context, Function2 predicate) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes($this$takeWhile), new C11671($this$takeWhile, predicate, null));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:25:0x008c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x009b A[Catch: all -> 0x00c1, TryCatch #0 {all -> 0x00c1, blocks: (B:27:0x0093, B:29:0x009b, B:34:0x00b7), top: B:48:0x0093 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00b7 A[Catch: all -> 0x00c1, TRY_LEAVE, TryCatch #0 {all -> 0x00c1, blocks: (B:27:0x0093, B:29:0x009b, B:34:0x00b7), top: B:48:0x0093 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v2, types: [java.lang.Object, kotlinx.coroutines.channels.SendChannel] */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v8, types: [kotlinx.coroutines.channels.SendChannel] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:32:0x00b0 -> B:33:0x00b5). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E, C extends SendChannel<? super E>> Object toChannel(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) {
        C11681 c11681;
        ReceiveChannel $this$consume$iv$iv;
        Throwable cause$iv$iv;
        ChannelIterator<? extends E> it;
        Continuation $continuation;
        Object obj;
        Object $result;
        C11681 c116812;
        Continuation continuation2;
        ChannelIterator<? extends E> channelIterator;
        ?? r7;
        Object objHasNext;
        if (continuation instanceof C11681) {
            c11681 = (C11681) continuation;
            if ((c11681.label & Integer.MIN_VALUE) != 0) {
                c11681.label -= Integer.MIN_VALUE;
            } else {
                c11681 = new C11681(continuation);
            }
        }
        C11681 c116813 = c11681;
        Object $result2 = c116813.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c116813.label) {
                case 0:
                    ResultKt.throwOnFailure($result2);
                    $this$consume$iv$iv = receiveChannel;
                    cause$iv$iv = null;
                    try {
                        it = $this$consume$iv$iv.iterator();
                        $continuation = null;
                        obj = coroutine_suspended;
                        $result = $result2;
                        c116812 = c116813;
                        try {
                            c116812.L$0 = c;
                            c116812.L$1 = $this$consume$iv$iv;
                            c116812.L$2 = it;
                            c116812.label = 1;
                            objHasNext = it.hasNext(c116812);
                            if (objHasNext == obj) {
                                return obj;
                            }
                            ChannelIterator<? extends E> channelIterator2 = it;
                            r7 = c;
                            continuation2 = $continuation;
                            c116813 = c116812;
                            $result2 = objHasNext;
                            channelIterator = channelIterator2;
                            try {
                                if (((Boolean) $result2).booleanValue()) {
                                    Unit unit = Unit.INSTANCE;
                                    return r7;
                                }
                                E next = channelIterator.next();
                                c116813.L$0 = r7;
                                c116813.L$1 = $this$consume$iv$iv;
                                c116813.L$2 = channelIterator;
                                c116813.label = 2;
                                if (r7.send(next, c116813) == obj) {
                                    return obj;
                                }
                                c116812 = c116813;
                                $continuation = continuation2;
                                c = r7;
                                it = channelIterator;
                                c116812.L$0 = c;
                                c116812.L$1 = $this$consume$iv$iv;
                                c116812.L$2 = it;
                                c116812.label = 1;
                                objHasNext = it.hasNext(c116812);
                                if (objHasNext == obj) {
                                }
                            } catch (Throwable th) {
                                e$iv$iv = th;
                                Throwable cause$iv$iv2 = e$iv$iv;
                                try {
                                    throw e$iv$iv;
                                } finally {
                                    ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv2);
                                }
                            }
                        } catch (Throwable th2) {
                            e$iv$iv = th2;
                            Throwable cause$iv$iv22 = e$iv$iv;
                            throw e$iv$iv;
                        }
                    } catch (Throwable th3) {
                        e$iv$iv = th3;
                        Throwable cause$iv$iv222 = e$iv$iv;
                        throw e$iv$iv;
                    }
                case 1:
                    continuation2 = null;
                    channelIterator = (ChannelIterator) c116813.L$2;
                    cause$iv$iv = null;
                    ReceiveChannel $this$consume$iv$iv2 = (ReceiveChannel) c116813.L$1;
                    $this$consume$iv$iv = $this$consume$iv$iv2;
                    SendChannel destination = (SendChannel) c116813.L$0;
                    ResultKt.throwOnFailure($result2);
                    r7 = destination;
                    obj = coroutine_suspended;
                    $result = $result2;
                    if (((Boolean) $result2).booleanValue()) {
                    }
                    break;
                case 2:
                    ChannelIterator<? extends E> channelIterator3 = (ChannelIterator) c116813.L$2;
                    cause$iv$iv = null;
                    $this$consume$iv$iv = (ReceiveChannel) c116813.L$1;
                    ?? r72 = (SendChannel) c116813.L$0;
                    ResultKt.throwOnFailure($result2);
                    $continuation = null;
                    c = r72;
                    it = channelIterator3;
                    obj = coroutine_suspended;
                    $result = $result2;
                    c116812 = c116813;
                    c116812.L$0 = c;
                    c116812.L$1 = $this$consume$iv$iv;
                    c116812.L$2 = it;
                    c116812.label = 1;
                    objHasNext = it.hasNext(c116812);
                    if (objHasNext == obj) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th4) {
            e$iv$iv = th4;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0070 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0082 A[Catch: all -> 0x009e, TryCatch #3 {all -> 0x009e, blocks: (B:24:0x007a, B:26:0x0082, B:27:0x0094), top: B:45:0x007a }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0094 A[Catch: all -> 0x009e, TRY_LEAVE, TryCatch #3 {all -> 0x009e, blocks: (B:24:0x007a, B:26:0x0082, B:27:0x0094), top: B:45:0x007a }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0071 -> B:45:0x007a). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E, C extends Collection<? super E>> Object toCollection(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) {
        C11691 c11691;
        ReceiveChannel $this$consume$iv$iv;
        Throwable cause$iv$iv;
        Object $result;
        Collection collection;
        ReceiveChannel $this$consume$iv$iv2;
        Throwable cause$iv$iv2;
        ChannelIterator channelIterator;
        int i;
        Object obj;
        if (continuation instanceof C11691) {
            c11691 = (C11691) continuation;
            if ((c11691.label & Integer.MIN_VALUE) != 0) {
                c11691.label -= Integer.MIN_VALUE;
            } else {
                c11691 = new C11691(continuation);
            }
        }
        C11691 c116912 = c11691;
        Object e$iv = c116912.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c116912.label) {
            case 0:
                ResultKt.throwOnFailure(e$iv);
                $this$consume$iv$iv = receiveChannel;
                Throwable cause$iv$iv3 = null;
                try {
                    ChannelIterator it = $this$consume$iv$iv.iterator();
                    int $i$f$consumeEach = 0;
                    Collection destination = c;
                    c116912.L$0 = destination;
                    c116912.L$1 = $this$consume$iv$iv;
                    c116912.L$2 = it;
                    c116912.label = 1;
                    Object objHasNext = it.hasNext(c116912);
                    if (objHasNext != $result2) {
                        return $result2;
                    }
                    Object obj2 = $result2;
                    $result = e$iv;
                    e$iv = objHasNext;
                    collection = destination;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv2 = cause$iv$iv3;
                    channelIterator = it;
                    i = $i$f$consumeEach;
                    obj = obj2;
                    try {
                        if (!((Boolean) e$iv).booleanValue()) {
                            collection.add(channelIterator.next());
                            e$iv = $result;
                            $result2 = obj;
                            $i$f$consumeEach = i;
                            it = channelIterator;
                            cause$iv$iv3 = cause$iv$iv;
                            $this$consume$iv$iv = $this$consume$iv$iv;
                            destination = collection;
                            c116912.L$0 = destination;
                            c116912.L$1 = $this$consume$iv$iv;
                            c116912.L$2 = it;
                            c116912.label = 1;
                            Object objHasNext2 = it.hasNext(c116912);
                            if (objHasNext2 != $result2) {
                            }
                        } else {
                            Unit unit = Unit.INSTANCE;
                            return collection;
                        }
                    } catch (Throwable th) {
                        $this$consume$iv$iv = $this$consume$iv$iv;
                        e$iv$iv = th;
                        cause$iv$iv = e$iv$iv;
                        try {
                            throw e$iv$iv;
                        } finally {
                            ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv);
                        }
                    }
                } catch (Throwable th2) {
                    e$iv$iv = th2;
                    cause$iv$iv = e$iv$iv;
                    throw e$iv$iv;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c116912.L$2;
                $this$consume$iv$iv = (ReceiveChannel) c116912.L$1;
                Collection destination2 = (Collection) c116912.L$0;
                try {
                    ResultKt.throwOnFailure(e$iv);
                    collection = destination2;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv2 = null;
                    channelIterator = channelIterator2;
                    i = 0;
                    obj = $result2;
                    $result = e$iv;
                    if (!((Boolean) e$iv).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv$iv = th3;
                    cause$iv$iv = e$iv$iv;
                    throw e$iv$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0070 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0082 A[Catch: all -> 0x00a8, TryCatch #1 {all -> 0x00a8, blocks: (B:24:0x007a, B:26:0x0082, B:27:0x009e), top: B:41:0x007a }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x009e A[Catch: all -> 0x00a8, TRY_LEAVE, TryCatch #1 {all -> 0x00a8, blocks: (B:24:0x007a, B:26:0x0082, B:27:0x009e), top: B:41:0x007a }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0071 -> B:41:0x007a). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <K, V, M extends Map<? super K, ? super V>> Object toMap(ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel, M m, Continuation<? super M> continuation) {
        C11702 c11702;
        ReceiveChannel $this$consume$iv$iv;
        Throwable cause$iv$iv;
        Object $result;
        Map map;
        ReceiveChannel $this$consume$iv$iv2;
        Throwable cause$iv$iv2;
        ChannelIterator channelIterator;
        int i;
        Object obj;
        if (continuation instanceof C11702) {
            c11702 = (C11702) continuation;
            if ((c11702.label & Integer.MIN_VALUE) != 0) {
                c11702.label -= Integer.MIN_VALUE;
            } else {
                c11702 = new C11702(continuation);
            }
        }
        C11702 c117022 = c11702;
        Object e$iv = c117022.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c117022.label) {
            case 0:
                ResultKt.throwOnFailure(e$iv);
                $this$consume$iv$iv = receiveChannel;
                Throwable cause$iv$iv3 = null;
                try {
                    ChannelIterator it = $this$consume$iv$iv.iterator();
                    int $i$f$consumeEach = 0;
                    Map destination = m;
                    c117022.L$0 = destination;
                    c117022.L$1 = $this$consume$iv$iv;
                    c117022.L$2 = it;
                    c117022.label = 1;
                    Object objHasNext = it.hasNext(c117022);
                    if (objHasNext != $result2) {
                        return $result2;
                    }
                    Object obj2 = $result2;
                    $result = e$iv;
                    e$iv = objHasNext;
                    map = destination;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv2 = cause$iv$iv3;
                    channelIterator = it;
                    i = $i$f$consumeEach;
                    obj = obj2;
                    try {
                        if (!((Boolean) e$iv).booleanValue()) {
                            Pair it2 = (Pair) channelIterator.next();
                            map.put(it2.getFirst(), it2.getSecond());
                            e$iv = $result;
                            $result2 = obj;
                            $i$f$consumeEach = i;
                            it = channelIterator;
                            cause$iv$iv3 = cause$iv$iv;
                            $this$consume$iv$iv = $this$consume$iv$iv;
                            destination = map;
                            c117022.L$0 = destination;
                            c117022.L$1 = $this$consume$iv$iv;
                            c117022.L$2 = it;
                            c117022.label = 1;
                            Object objHasNext2 = it.hasNext(c117022);
                            if (objHasNext2 != $result2) {
                            }
                        } else {
                            Unit unit = Unit.INSTANCE;
                            return map;
                        }
                    } catch (Throwable th) {
                        $this$consume$iv$iv = $this$consume$iv$iv;
                        e$iv$iv = th;
                        cause$iv$iv = e$iv$iv;
                        try {
                            throw e$iv$iv;
                        } finally {
                            ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv);
                        }
                    }
                } catch (Throwable th2) {
                    e$iv$iv = th2;
                    cause$iv$iv = e$iv$iv;
                    throw e$iv$iv;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c117022.L$2;
                $this$consume$iv$iv = (ReceiveChannel) c117022.L$1;
                Map destination2 = (Map) c117022.L$0;
                try {
                    ResultKt.throwOnFailure(e$iv);
                    map = destination2;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv2 = null;
                    channelIterator = channelIterator2;
                    i = 0;
                    obj = $result2;
                    $result = e$iv;
                    if (!((Boolean) e$iv).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv$iv = th3;
                    cause$iv$iv = e$iv$iv;
                    throw e$iv$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ ReceiveChannel flatMap$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return flatMap(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$flatMap$1", m162f = "Deprecated.kt", m163i = {0, 1, 2}, m164l = {351, 352, 352}, m165m = "invokeSuspend", m166n = {"$this$produce", "$this$produce", "$this$produce"}, m167s = {"L$0", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$flatMap$1 */
    static final class C11531<R> extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_flatMap;
        final /* synthetic */ Function2<E, Continuation<? super ReceiveChannel<? extends R>>, Object> $transform;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11531(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super ReceiveChannel<? extends R>>, ? extends Object> function2, Continuation<? super C11531> continuation) {
            super(2, continuation);
            this.$this_flatMap = receiveChannel;
            this.$transform = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11531 c11531 = new C11531(this.$this_flatMap, this.$transform, continuation);
            c11531.L$0 = obj;
            return c11531;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super R> producerScope, Continuation<? super Unit> continuation) {
            return ((C11531) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0065 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0066  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0075  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x009e A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:22:0x009f  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00a5  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x009f -> B:10:0x0055). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11531 c11531;
            ProducerScope $this$produce;
            ChannelIterator it;
            ProducerScope $this$produce2;
            ChannelIterator channelIterator;
            C11531 c115312;
            Object obj;
            Object $result2;
            Object objHasNext;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11531 = this;
                    ProducerScope $this$produce3 = (ProducerScope) c11531.L$0;
                    $this$produce = $this$produce3;
                    it = c11531.$this_flatMap.iterator();
                    c11531.L$0 = $this$produce;
                    c11531.L$1 = it;
                    c11531.label = 1;
                    objHasNext = it.hasNext(c11531);
                    if (objHasNext == $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext;
                    $this$produce2 = $this$produce;
                    channelIterator = it;
                    c115312 = c11531;
                    obj = obj2;
                    if (((Boolean) $result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    Object e = channelIterator.next();
                    Function2<E, Continuation<? super ReceiveChannel<? extends R>>, Object> function2 = c115312.$transform;
                    c115312.L$0 = $this$produce2;
                    c115312.L$1 = channelIterator;
                    c115312.label = 2;
                    $result = function2.invoke(e, c115312);
                    if ($result == obj) {
                        return obj;
                    }
                    c115312.L$0 = $this$produce2;
                    c115312.L$1 = channelIterator;
                    c115312.label = 3;
                    if (ChannelsKt.toChannel((ReceiveChannel) $result, $this$produce2, c115312) != obj) {
                        return obj;
                    }
                    $result = $result2;
                    $result3 = obj;
                    c11531 = c115312;
                    it = channelIterator;
                    $this$produce = $this$produce2;
                    c11531.L$0 = $this$produce;
                    c11531.L$1 = it;
                    c11531.label = 1;
                    objHasNext = it.hasNext(c11531);
                    if (objHasNext == $result3) {
                    }
                case 1:
                    ChannelIterator channelIterator2 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce4 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce2 = $this$produce4;
                    channelIterator = channelIterator2;
                    c115312 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 2:
                    ChannelIterator channelIterator3 = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce5 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce2 = $this$produce5;
                    channelIterator = channelIterator3;
                    c115312 = this;
                    obj = $result3;
                    $result2 = $result;
                    c115312.L$0 = $this$produce2;
                    c115312.L$1 = channelIterator;
                    c115312.label = 3;
                    if (ChannelsKt.toChannel((ReceiveChannel) $result, $this$produce2, c115312) != obj) {
                    }
                    break;
                case 3:
                    c11531 = this;
                    it = (ChannelIterator) c11531.L$1;
                    $this$produce = (ProducerScope) c11531.L$0;
                    ResultKt.throwOnFailure($result);
                    c11531.L$0 = $this$produce;
                    c11531.L$1 = it;
                    c11531.label = 1;
                    objHasNext = it.hasNext(c11531);
                    if (objHasNext == $result3) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel flatMap(ReceiveChannel $this$flatMap, CoroutineContext context, Function2 transform) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes($this$flatMap), new C11531($this$flatMap, transform, null));
    }

    public static /* synthetic */ ReceiveChannel map$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.map(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$map$1", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1, 2, 2}, m164l = {517, 363, 363}, m165m = "invokeSuspend", m166n = {"$this$produce", "$this$consume$iv$iv", "$this$produce", "$this$consume$iv$iv", "$this$produce", "$this$consume$iv$iv"}, m167s = {"L$0", "L$2", "L$0", "L$2", "L$0", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$map$1 */
    static final class C11581<R> extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_map;
        final /* synthetic */ Function2<E, Continuation<? super R>, Object> $transform;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11581(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2, Continuation<? super C11581> continuation) {
            super(2, continuation);
            this.$this_map = receiveChannel;
            this.$transform = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11581 c11581 = new C11581(this.$this_map, this.$transform, continuation);
            c11581.L$0 = obj;
            return c11581;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super R> producerScope, Continuation<? super Unit> continuation) {
            return ((C11581) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x00ba A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00bb  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00cc A[Catch: all -> 0x011c, TryCatch #3 {all -> 0x011c, blocks: (B:25:0x00c4, B:27:0x00cc, B:31:0x00ec, B:36:0x0110), top: B:53:0x00c4 }] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0100 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0101  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0110 A[Catch: all -> 0x011c, TRY_LEAVE, TryCatch #3 {all -> 0x011c, blocks: (B:25:0x00c4, B:27:0x00cc, B:31:0x00ec, B:36:0x0110), top: B:53:0x00c4 }] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x0101 -> B:35:0x010c). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) {
            ReceiveChannel $this$consume$iv$iv;
            C11581 c11581;
            Object $result;
            ProducerScope $this$produce;
            int $i$f$consumeEach;
            Throwable cause$iv$iv;
            ChannelIterator it;
            Function2 function2;
            Object $result2;
            Object $result3;
            Function2 function22;
            ProducerScope $this$produce2;
            ReceiveChannel $this$consume$iv$iv2;
            Throwable cause$iv$iv2;
            Object $result4;
            ChannelIterator channelIterator;
            Function2 function23;
            ProducerScope $this$produce3;
            ProducerScope $this$produce4;
            int $i$f$consumeEach2;
            Function2 function24;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        c11581 = this;
                        $result = obj;
                        $this$produce = (ProducerScope) c11581.L$0;
                        ReceiveChannel $this$consumeEach$iv = c11581.$this_map;
                        Function2 function25 = c11581.$transform;
                        $i$f$consumeEach = 0;
                        $this$consume$iv$iv = $this$consumeEach$iv;
                        cause$iv$iv = null;
                        try {
                            it = $this$consume$iv$iv.iterator();
                            function2 = function25;
                            c11581.L$0 = $this$produce;
                            c11581.L$1 = function2;
                            c11581.L$2 = $this$consume$iv$iv;
                            c11581.L$3 = it;
                            c11581.label = 1;
                            objHasNext = it.hasNext(c11581);
                            if (objHasNext == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            ProducerScope producerScope = $this$produce;
                            $result3 = $result;
                            $result2 = objHasNext;
                            $this$consume$iv$iv2 = $this$consume$iv$iv;
                            cause$iv$iv2 = cause$iv$iv;
                            function22 = function2;
                            $this$produce2 = producerScope;
                            try {
                                if (((Boolean) $result2).booleanValue()) {
                                    Unit unit = Unit.INSTANCE;
                                    ChannelsKt.cancelConsumed($this$consume$iv$iv2, cause$iv$iv2);
                                    return Unit.INSTANCE;
                                }
                                Object it2 = it.next();
                                $this$produce4 = null;
                                c11581.L$0 = $this$produce2;
                                c11581.L$1 = function22;
                                c11581.L$2 = $this$consume$iv$iv2;
                                c11581.L$3 = it;
                                c11581.L$4 = $this$produce2;
                                c11581.label = 2;
                                Object objInvoke = function22.invoke(it2, c11581);
                                if (objInvoke == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                $result4 = objInvoke;
                                $this$produce3 = $this$produce2;
                                ChannelIterator channelIterator2 = it;
                                function23 = function22;
                                channelIterator = channelIterator2;
                                c11581.L$0 = $this$produce3;
                                c11581.L$1 = function23;
                                c11581.L$2 = $this$consume$iv$iv2;
                                c11581.L$3 = channelIterator;
                                c11581.L$4 = null;
                                c11581.label = 3;
                                if ($this$produce2.send($result4, c11581) != coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                $result = $result3;
                                $i$f$consumeEach2 = $i$f$consumeEach;
                                $this$produce = $this$produce3;
                                Function2 function26 = function23;
                                it = channelIterator;
                                cause$iv$iv = cause$iv$iv2;
                                $this$consume$iv$iv = $this$consume$iv$iv2;
                                function24 = function26;
                                $i$f$consumeEach = $i$f$consumeEach2;
                                function2 = function24;
                                c11581.L$0 = $this$produce;
                                c11581.L$1 = function2;
                                c11581.L$2 = $this$consume$iv$iv;
                                c11581.L$3 = it;
                                c11581.label = 1;
                                objHasNext = it.hasNext(c11581);
                                if (objHasNext == coroutine_suspended) {
                                }
                            } catch (Throwable th) {
                                e$iv$iv = th;
                                $this$consume$iv$iv = $this$consume$iv$iv2;
                                Throwable cause$iv$iv3 = e$iv$iv;
                                try {
                                    throw e$iv$iv;
                                } catch (Throwable e$iv$iv) {
                                    ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv3);
                                    throw e$iv$iv;
                                }
                            }
                        } catch (Throwable th2) {
                            e$iv$iv = th2;
                            Throwable cause$iv$iv32 = e$iv$iv;
                            throw e$iv$iv;
                        }
                    case 1:
                        c11581 = this;
                        $result2 = obj;
                        ChannelIterator channelIterator3 = (ChannelIterator) c11581.L$3;
                        ReceiveChannel $this$consume$iv$iv3 = (ReceiveChannel) c11581.L$2;
                        Function2 function27 = (Function2) c11581.L$1;
                        ProducerScope $this$produce5 = (ProducerScope) c11581.L$0;
                        ResultKt.throwOnFailure($result2);
                        it = channelIterator3;
                        $i$f$consumeEach = 0;
                        $result3 = $result2;
                        function22 = function27;
                        $this$produce2 = $this$produce5;
                        $this$consume$iv$iv2 = $this$consume$iv$iv3;
                        cause$iv$iv2 = null;
                        if (((Boolean) $result2).booleanValue()) {
                        }
                        break;
                    case 2:
                        c11581 = this;
                        $result4 = obj;
                        $this$produce2 = (ProducerScope) c11581.L$4;
                        channelIterator = (ChannelIterator) c11581.L$3;
                        cause$iv$iv2 = null;
                        $this$consume$iv$iv2 = (ReceiveChannel) c11581.L$2;
                        function23 = (Function2) c11581.L$1;
                        ProducerScope $this$produce6 = (ProducerScope) c11581.L$0;
                        try {
                            ResultKt.throwOnFailure($result4);
                            $this$produce3 = $this$produce6;
                            $this$produce4 = null;
                            $i$f$consumeEach = 0;
                            $result3 = $result4;
                            c11581.L$0 = $this$produce3;
                            c11581.L$1 = function23;
                            c11581.L$2 = $this$consume$iv$iv2;
                            c11581.L$3 = channelIterator;
                            c11581.L$4 = null;
                            c11581.label = 3;
                            if ($this$produce2.send($result4, c11581) != coroutine_suspended) {
                            }
                        } catch (Throwable th3) {
                            e$iv$iv = th3;
                            $this$consume$iv$iv = $this$consume$iv$iv2;
                            Throwable cause$iv$iv322 = e$iv$iv;
                            throw e$iv$iv;
                        }
                        break;
                    case 3:
                        c11581 = this;
                        $result = obj;
                        ChannelIterator channelIterator4 = (ChannelIterator) c11581.L$3;
                        cause$iv$iv = null;
                        $this$consume$iv$iv = (ReceiveChannel) c11581.L$2;
                        function24 = (Function2) c11581.L$1;
                        ProducerScope $this$produce7 = (ProducerScope) c11581.L$0;
                        ResultKt.throwOnFailure($result);
                        $i$f$consumeEach2 = 0;
                        $this$produce = $this$produce7;
                        it = channelIterator4;
                        $i$f$consumeEach = $i$f$consumeEach2;
                        function2 = function24;
                        c11581.L$0 = $this$produce;
                        c11581.L$1 = function2;
                        c11581.L$2 = $this$consume$iv$iv;
                        c11581.L$3 = it;
                        c11581.label = 1;
                        objHasNext = it.hasNext(c11581);
                        if (objHasNext == coroutine_suspended) {
                        }
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            } catch (Throwable th4) {
                e$iv$iv = th4;
            }
        }
    }

    public static final <E, R> ReceiveChannel<R> map(ReceiveChannel<? extends E> receiveChannel, CoroutineContext context, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes(receiveChannel), new C11581(receiveChannel, function2, null));
    }

    public static /* synthetic */ ReceiveChannel mapIndexed$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.mapIndexed(receiveChannel, coroutineContext, function3);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$mapIndexed$1", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1, 2, 2}, m164l = {374, 375, 375}, m165m = "invokeSuspend", m166n = {"$this$produce", "index", "$this$produce", "index", "$this$produce", "index"}, m167s = {"L$0", "I$0", "L$0", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$mapIndexed$1 */
    static final class C11591<R> extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_mapIndexed;
        final /* synthetic */ Function3<Integer, E, Continuation<? super R>, Object> $transform;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11591(ReceiveChannel<? extends E> receiveChannel, Function3<? super Integer, ? super E, ? super Continuation<? super R>, ? extends Object> function3, Continuation<? super C11591> continuation) {
            super(2, continuation);
            this.$this_mapIndexed = receiveChannel;
            this.$transform = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11591 c11591 = new C11591(this.$this_mapIndexed, this.$transform, continuation);
            c11591.L$0 = obj;
            return c11591;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super R> producerScope, Continuation<? super Unit> continuation) {
            return ((C11591) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0072 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0073  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0082  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x00b8 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:23:0x00b9  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00bf  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x00b9 -> B:10:0x0060). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11591 c11591;
            ProducerScope $this$produce;
            int index;
            ChannelIterator it;
            ProducerScope $this$produce2;
            ChannelIterator channelIterator;
            C11591 c115912;
            Object obj;
            Object $result2;
            ProducerScope producerScope;
            ProducerScope $this$produce3;
            int index2;
            Object objHasNext;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11591 = this;
                    $this$produce = (ProducerScope) c11591.L$0;
                    index = 0;
                    it = c11591.$this_mapIndexed.iterator();
                    c11591.L$0 = $this$produce;
                    c11591.L$1 = it;
                    c11591.I$0 = index;
                    c11591.label = 1;
                    objHasNext = it.hasNext(c11591);
                    if (objHasNext == $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext;
                    channelIterator = it;
                    $this$produce2 = $this$produce;
                    c115912 = c11591;
                    obj = obj2;
                    if (((Boolean) $result).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                    Object e = channelIterator.next();
                    Function3<Integer, E, Continuation<? super R>, Object> function3 = c115912.$transform;
                    index2 = index + 1;
                    Integer numBoxInt = Boxing.boxInt(index);
                    c115912.L$0 = $this$produce2;
                    c115912.L$1 = channelIterator;
                    c115912.L$2 = $this$produce2;
                    c115912.I$0 = index2;
                    c115912.label = 2;
                    $result = function3.invoke(numBoxInt, e, c115912);
                    if ($result == obj) {
                        return obj;
                    }
                    producerScope = $this$produce2;
                    it = channelIterator;
                    $this$produce3 = producerScope;
                    c115912.L$0 = $this$produce3;
                    c115912.L$1 = it;
                    c115912.L$2 = null;
                    c115912.I$0 = index2;
                    c115912.label = 3;
                    if (producerScope.send($result, c115912) != obj) {
                        return obj;
                    }
                    $result = $result2;
                    $result3 = obj;
                    c11591 = c115912;
                    $this$produce = $this$produce3;
                    index = index2;
                    c11591.L$0 = $this$produce;
                    c11591.L$1 = it;
                    c11591.I$0 = index;
                    c11591.label = 1;
                    objHasNext = it.hasNext(c11591);
                    if (objHasNext == $result3) {
                    }
                case 1:
                    int index3 = this.I$0;
                    ChannelIterator channelIterator2 = (ChannelIterator) this.L$1;
                    $this$produce2 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    channelIterator = channelIterator2;
                    index = index3;
                    c115912 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 2:
                    int index4 = this.I$0;
                    producerScope = (ProducerScope) this.L$2;
                    it = (ChannelIterator) this.L$1;
                    $this$produce3 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    index2 = index4;
                    c115912 = this;
                    obj = $result3;
                    $result2 = $result;
                    c115912.L$0 = $this$produce3;
                    c115912.L$1 = it;
                    c115912.L$2 = null;
                    c115912.I$0 = index2;
                    c115912.label = 3;
                    if (producerScope.send($result, c115912) != obj) {
                    }
                    break;
                case 3:
                    c11591 = this;
                    int index5 = c11591.I$0;
                    ChannelIterator channelIterator3 = (ChannelIterator) c11591.L$1;
                    ProducerScope $this$produce4 = (ProducerScope) c11591.L$0;
                    ResultKt.throwOnFailure($result);
                    index = index5;
                    $this$produce = $this$produce4;
                    it = channelIterator3;
                    c11591.L$0 = $this$produce;
                    c11591.L$1 = it;
                    c11591.I$0 = index;
                    c11591.label = 1;
                    objHasNext = it.hasNext(c11591);
                    if (objHasNext == $result3) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <E, R> ReceiveChannel<R> mapIndexed(ReceiveChannel<? extends E> receiveChannel, CoroutineContext context, Function3<? super Integer, ? super E, ? super Continuation<? super R>, ? extends Object> function3) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes(receiveChannel), new C11591(receiveChannel, function3, null));
    }

    public static /* synthetic */ ReceiveChannel mapIndexedNotNull$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return mapIndexedNotNull(receiveChannel, coroutineContext, function3);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel mapIndexedNotNull(ReceiveChannel $this$mapIndexedNotNull, CoroutineContext context, Function3 transform) {
        return ChannelsKt.filterNotNull(ChannelsKt.mapIndexed($this$mapIndexedNotNull, context, transform));
    }

    public static /* synthetic */ ReceiveChannel mapNotNull$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return mapNotNull(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel mapNotNull(ReceiveChannel $this$mapNotNull, CoroutineContext context, Function2 transform) {
        return ChannelsKt.filterNotNull(ChannelsKt.map($this$mapNotNull, context, transform));
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlin/collections/IndexedValue;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$withIndex$1", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1}, m164l = {400, TypedValues.CycleType.TYPE_CURVE_FIT}, m165m = "invokeSuspend", m166n = {"$this$produce", "index", "$this$produce", "index"}, m167s = {"L$0", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$withIndex$1 */
    static final class C11711<E> extends SuspendLambda implements Function2<ProducerScope<? super IndexedValue<? extends E>>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_withIndex;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11711(ReceiveChannel<? extends E> receiveChannel, Continuation<? super C11711> continuation) {
            super(2, continuation);
            this.$this_withIndex = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11711 c11711 = new C11711(this.$this_withIndex, continuation);
            c11711.L$0 = obj;
            return c11711;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super IndexedValue<? extends E>> producerScope, Continuation<? super Unit> continuation) {
            return ((C11711) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x005b A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:12:0x005c  */
        /* JADX WARN: Removed duplicated region for block: B:15:0x006a  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x008e  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x0088 -> B:9:0x0049). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11711 c11711;
            ProducerScope $this$produce;
            int index;
            ChannelIterator<E> it;
            ProducerScope $this$produce2;
            C11711 c117112;
            Object obj;
            Object $result2;
            Object objHasNext;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11711 = this;
                    $this$produce = (ProducerScope) c11711.L$0;
                    index = 0;
                    it = c11711.$this_withIndex.iterator();
                    c11711.L$0 = $this$produce;
                    c11711.L$1 = it;
                    c11711.I$0 = index;
                    c11711.label = 1;
                    objHasNext = it.hasNext(c11711);
                    if (objHasNext == $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext;
                    $this$produce2 = $this$produce;
                    c117112 = c11711;
                    obj = obj2;
                    if (!((Boolean) $result).booleanValue()) {
                        Object e = it.next();
                        int index2 = index + 1;
                        c117112.L$0 = $this$produce2;
                        c117112.L$1 = it;
                        c117112.I$0 = index2;
                        c117112.label = 2;
                        if ($this$produce2.send(new IndexedValue(index, e), c117112) == obj) {
                            return obj;
                        }
                        $result = $result2;
                        $result3 = obj;
                        c11711 = c117112;
                        $this$produce = $this$produce2;
                        index = index2;
                        c11711.L$0 = $this$produce;
                        c11711.L$1 = it;
                        c11711.I$0 = index;
                        c11711.label = 1;
                        objHasNext = it.hasNext(c11711);
                        if (objHasNext == $result3) {
                        }
                    } else {
                        return Unit.INSTANCE;
                    }
                case 1:
                    int index3 = this.I$0;
                    ChannelIterator<E> channelIterator = (ChannelIterator) this.L$1;
                    ProducerScope $this$produce3 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce2 = $this$produce3;
                    it = channelIterator;
                    index = index3;
                    c117112 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (!((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 2:
                    c11711 = this;
                    int index4 = c11711.I$0;
                    ChannelIterator<E> channelIterator2 = (ChannelIterator) c11711.L$1;
                    ProducerScope $this$produce4 = (ProducerScope) c11711.L$0;
                    ResultKt.throwOnFailure($result);
                    index = index4;
                    $this$produce = $this$produce4;
                    it = channelIterator2;
                    c11711.L$0 = $this$produce;
                    c11711.L$1 = it;
                    c11711.I$0 = index;
                    c11711.label = 1;
                    objHasNext = it.hasNext(c11711);
                    if (objHasNext == $result3) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static /* synthetic */ ReceiveChannel withIndex$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return withIndex(receiveChannel, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel withIndex(ReceiveChannel $this$withIndex, CoroutineContext context) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes($this$withIndex), new C11711($this$withIndex, null));
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u0004\n\u0002\b\u0003\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u0002H\u0001H\u008a@"}, m146d2 = {"<anonymous>", "E", "it"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinct$1", m162f = "Deprecated.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinct$1 */
    static final class C11391<E> extends SuspendLambda implements Function2<E, Continuation<? super E>, Object> {
        /* synthetic */ Object L$0;
        int label;

        C11391(Continuation<? super C11391> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11391 c11391 = new C11391(continuation);
            c11391.L$0 = obj;
            return c11391;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
            return invoke((C11391<E>) obj, (Continuation<? super C11391<E>>) obj2);
        }

        public final Object invoke(E e, Continuation<? super E> continuation) {
            return ((C11391) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    Object it = this.L$0;
                    return it;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static /* synthetic */ ReceiveChannel distinctBy$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.distinctBy(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "K", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinctBy$1", m162f = "Deprecated.kt", m163i = {0, 0, 1, 1, 1, 2, 2, 2}, m164l = {417, 418, TypedValues.CycleType.TYPE_EASING}, m165m = "invokeSuspend", m166n = {"$this$produce", "keys", "$this$produce", "keys", "e", "$this$produce", "keys", "k"}, m167s = {"L$0", "L$1", "L$0", "L$1", "L$3", "L$0", "L$1", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinctBy$1 */
    static final class C11401<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super K>, Object> $selector;
        final /* synthetic */ ReceiveChannel<E> $this_distinctBy;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11401(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super K>, ? extends Object> function2, Continuation<? super C11401> continuation) {
            super(2, continuation);
            this.$this_distinctBy = receiveChannel;
            this.$selector = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11401 c11401 = new C11401(this.$this_distinctBy, this.$selector, continuation);
            c11401.L$0 = obj;
            return c11401;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C11401) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0082 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0093  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x00b8  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00df  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00e6  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x00cd -> B:26:0x00d5). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x00df -> B:10:0x006d). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            C11401 c11401;
            HashSet keys;
            ProducerScope $this$produce;
            ChannelIterator<E> it;
            ProducerScope $this$produce2;
            HashSet keys2;
            ChannelIterator<E> channelIterator;
            C11401 c114012;
            Object obj;
            Object $result2;
            ProducerScope $this$produce3;
            HashSet keys3;
            ChannelIterator<E> channelIterator2;
            E e;
            ChannelIterator<E> channelIterator3;
            HashSet keys4;
            ProducerScope $this$produce4;
            Object k;
            Object objHasNext;
            Object $result3 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c11401 = this;
                    ProducerScope $this$produce5 = (ProducerScope) c11401.L$0;
                    keys = new HashSet();
                    $this$produce = $this$produce5;
                    it = c11401.$this_distinctBy.iterator();
                    c11401.L$0 = $this$produce;
                    c11401.L$1 = keys;
                    c11401.L$2 = it;
                    c11401.L$3 = null;
                    c11401.label = 1;
                    objHasNext = it.hasNext(c11401);
                    if (objHasNext != $result3) {
                        return $result3;
                    }
                    Object obj2 = $result3;
                    $result2 = $result;
                    $result = objHasNext;
                    $this$produce2 = $this$produce;
                    keys2 = keys;
                    channelIterator = it;
                    c114012 = c11401;
                    obj = obj2;
                    if (((Boolean) $result).booleanValue()) {
                        Object k2 = Unit.INSTANCE;
                        return k2;
                    }
                    E next = channelIterator.next();
                    Function2<E, Continuation<? super K>, Object> function2 = c114012.$selector;
                    c114012.L$0 = $this$produce2;
                    c114012.L$1 = keys2;
                    c114012.L$2 = channelIterator;
                    c114012.L$3 = next;
                    c114012.label = 2;
                    Object objInvoke = function2.invoke(next, c114012);
                    if (objInvoke == obj) {
                        return obj;
                    }
                    ChannelIterator<E> channelIterator4 = channelIterator;
                    e = next;
                    $result = objInvoke;
                    $this$produce3 = $this$produce2;
                    keys3 = keys2;
                    channelIterator2 = channelIterator4;
                    if (keys3.contains($result)) {
                        c114012.L$0 = $this$produce3;
                        c114012.L$1 = keys3;
                        c114012.L$2 = channelIterator2;
                        c114012.L$3 = $result;
                        c114012.label = 3;
                        Object e2 = $this$produce3.send(e, c114012);
                        if (e2 == obj) {
                            return obj;
                        }
                        channelIterator3 = channelIterator2;
                        keys4 = keys3;
                        $this$produce4 = $this$produce3;
                        C11401 c114013 = c114012;
                        k = $result;
                        $result = $result2;
                        $result3 = obj;
                        c11401 = c114013;
                        keys4.add(k);
                        it = channelIterator3;
                        keys = keys4;
                        $this$produce = $this$produce4;
                        c11401.L$0 = $this$produce;
                        c11401.L$1 = keys;
                        c11401.L$2 = it;
                        c11401.L$3 = null;
                        c11401.label = 1;
                        objHasNext = it.hasNext(c11401);
                        if (objHasNext != $result3) {
                        }
                    } else {
                        $result = $result2;
                        $result3 = obj;
                        c11401 = c114012;
                        it = channelIterator2;
                        keys = keys3;
                        $this$produce = $this$produce3;
                        c11401.L$0 = $this$produce;
                        c11401.L$1 = keys;
                        c11401.L$2 = it;
                        c11401.L$3 = null;
                        c11401.label = 1;
                        objHasNext = it.hasNext(c11401);
                        if (objHasNext != $result3) {
                        }
                    }
                case 1:
                    ChannelIterator<E> channelIterator5 = (ChannelIterator) this.L$2;
                    HashSet keys5 = (HashSet) this.L$1;
                    ProducerScope $this$produce6 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce2 = $this$produce6;
                    keys2 = keys5;
                    channelIterator = channelIterator5;
                    c114012 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    break;
                case 2:
                    Object obj3 = this.L$3;
                    ChannelIterator<E> channelIterator6 = (ChannelIterator) this.L$2;
                    HashSet keys6 = (HashSet) this.L$1;
                    ProducerScope $this$produce7 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    $this$produce3 = $this$produce7;
                    keys3 = keys6;
                    channelIterator2 = channelIterator6;
                    e = obj3;
                    c114012 = this;
                    obj = $result3;
                    $result2 = $result;
                    if (keys3.contains($result)) {
                    }
                    break;
                case 3:
                    c11401 = this;
                    k = c11401.L$3;
                    channelIterator3 = (ChannelIterator) c11401.L$2;
                    keys4 = (HashSet) c11401.L$1;
                    $this$produce4 = (ProducerScope) c11401.L$0;
                    ResultKt.throwOnFailure($result);
                    keys4.add(k);
                    it = channelIterator3;
                    keys = keys4;
                    $this$produce = $this$produce4;
                    c11401.L$0 = $this$produce;
                    c11401.L$1 = keys;
                    c11401.L$2 = it;
                    c11401.L$3 = null;
                    c11401.label = 1;
                    objHasNext = it.hasNext(c11401);
                    if (objHasNext != $result3) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <E, K> ReceiveChannel<E> distinctBy(ReceiveChannel<? extends E> receiveChannel, CoroutineContext context, Function2<? super E, ? super Continuation<? super K>, ? extends Object> function2) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumes(receiveChannel), new C11401(receiveChannel, function2, null));
    }

    public static final <E> Object toMutableSet(ReceiveChannel<? extends E> receiveChannel, Continuation<? super Set<E>> continuation) {
        return ChannelsKt.toCollection(receiveChannel, new LinkedHashSet(), continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object any(ReceiveChannel $this$any, Continuation continuation) {
        C11341 c11341;
        ReceiveChannel $this$consume$iv;
        Throwable cause$iv;
        Object objHasNext;
        if (continuation instanceof C11341) {
            c11341 = (C11341) continuation;
            if ((c11341.label & Integer.MIN_VALUE) != 0) {
                c11341.label -= Integer.MIN_VALUE;
            } else {
                c11341 = new C11341(continuation);
            }
        }
        C11341 c113412 = c11341;
        Object $result = c113412.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c113412.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$consume$iv = $this$any;
                cause$iv = null;
                try {
                    ChannelIterator it = $this$consume$iv.iterator();
                    c113412.L$0 = $this$consume$iv;
                    c113412.label = 1;
                    objHasNext = it.hasNext(c113412);
                    if (objHasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return objHasNext;
                } catch (Throwable th) {
                    e$iv = th;
                    Throwable cause$iv2 = e$iv;
                    try {
                        throw e$iv;
                    } finally {
                        ChannelsKt.cancelConsumed($this$consume$iv, cause$iv2);
                    }
                }
            case 1:
                $this$consume$iv = (ReceiveChannel) c113412.L$0;
                cause$iv = null;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext = $result;
                    return objHasNext;
                } catch (Throwable th2) {
                    e$iv = th2;
                    Throwable cause$iv22 = e$iv;
                    throw e$iv;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0076 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0089 A[Catch: all -> 0x00ac, TryCatch #3 {all -> 0x00ac, blocks: (B:24:0x0081, B:26:0x0089, B:27:0x009c), top: B:45:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x009c A[Catch: all -> 0x00ac, TRY_LEAVE, TryCatch #3 {all -> 0x00ac, blocks: (B:24:0x0081, B:26:0x0089, B:27:0x009c), top: B:45:0x0081 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0077 -> B:45:0x0081). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object count(ReceiveChannel $this$consumeEach$iv, Continuation continuation) {
        C11381 c11381;
        ReceiveChannel $this$consume$iv$iv;
        Object $result;
        Ref.IntRef count;
        ReceiveChannel $this$consume$iv$iv2;
        Throwable cause$iv$iv;
        ChannelIterator channelIterator;
        int i;
        Ref.IntRef intRef;
        Object obj;
        if (continuation instanceof C11381) {
            c11381 = (C11381) continuation;
            if ((c11381.label & Integer.MIN_VALUE) != 0) {
                c11381.label -= Integer.MIN_VALUE;
            } else {
                c11381 = new C11381(continuation);
            }
        }
        C11381 c113812 = c11381;
        Object e$iv = c113812.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c113812.label) {
            case 0:
                ResultKt.throwOnFailure(e$iv);
                Ref.IntRef count2 = new Ref.IntRef();
                $this$consume$iv$iv = $this$consumeEach$iv;
                Throwable cause$iv$iv2 = null;
                try {
                    ChannelIterator it = $this$consume$iv$iv.iterator();
                    Ref.IntRef count3 = null;
                    int $i$f$consume = 0;
                    Ref.IntRef count4 = count2;
                    c113812.L$0 = count4;
                    c113812.L$1 = $this$consume$iv$iv;
                    c113812.L$2 = it;
                    c113812.label = 1;
                    Object objHasNext = it.hasNext(c113812);
                    if (objHasNext != $result2) {
                        return $result2;
                    }
                    Object obj2 = $result2;
                    $result = e$iv;
                    e$iv = objHasNext;
                    count = count4;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv = cause$iv$iv2;
                    channelIterator = it;
                    i = $i$f$consume;
                    intRef = count3;
                    obj = obj2;
                    try {
                        if (!((Boolean) e$iv).booleanValue()) {
                            channelIterator.next();
                            count.element++;
                            e$iv = $result;
                            $result2 = obj;
                            count3 = intRef;
                            $i$f$consume = i;
                            it = channelIterator;
                            cause$iv$iv2 = cause$iv$iv;
                            $this$consume$iv$iv = $this$consume$iv$iv2;
                            count4 = count;
                            c113812.L$0 = count4;
                            c113812.L$1 = $this$consume$iv$iv;
                            c113812.L$2 = it;
                            c113812.label = 1;
                            Object objHasNext2 = it.hasNext(c113812);
                            if (objHasNext2 != $result2) {
                            }
                        } else {
                            Unit unit = Unit.INSTANCE;
                            ChannelsKt.cancelConsumed($this$consume$iv$iv2, cause$iv$iv);
                            int $i$f$consumeEach = count.element;
                            return Boxing.boxInt($i$f$consumeEach);
                        }
                    } catch (Throwable th) {
                        $this$consume$iv$iv = $this$consume$iv$iv2;
                        e$iv$iv = th;
                        Throwable cause$iv$iv3 = e$iv$iv;
                        try {
                            throw e$iv$iv;
                        } catch (Throwable e$iv$iv) {
                            ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv3);
                            throw e$iv$iv;
                        }
                    }
                } catch (Throwable th2) {
                    e$iv$iv = th2;
                    Throwable cause$iv$iv32 = e$iv$iv;
                    throw e$iv$iv;
                }
            case 1:
                ChannelIterator channelIterator2 = (ChannelIterator) c113812.L$2;
                $this$consume$iv$iv = (ReceiveChannel) c113812.L$1;
                Ref.IntRef count5 = (Ref.IntRef) c113812.L$0;
                try {
                    ResultKt.throwOnFailure(e$iv);
                    count = count5;
                    $this$consume$iv$iv2 = $this$consume$iv$iv;
                    cause$iv$iv = null;
                    channelIterator = channelIterator2;
                    i = 0;
                    intRef = null;
                    obj = $result2;
                    $result = e$iv;
                    if (!((Boolean) e$iv).booleanValue()) {
                    }
                } catch (Throwable th3) {
                    e$iv$iv = th3;
                    Throwable cause$iv$iv322 = e$iv$iv;
                    throw e$iv$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b5 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c7 A[Catch: all -> 0x00eb, TRY_LEAVE, TryCatch #6 {all -> 0x00eb, blocks: (B:38:0x00bf, B:40:0x00c7), top: B:73:0x00bf }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:37:0x00b6 -> B:73:0x00bf). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object maxWith(ReceiveChannel $this$consume$iv, Comparator comparator, Continuation continuation) {
        C11601 c11601;
        Throwable cause$iv;
        ReceiveChannel $this$consume$iv2;
        Throwable e$iv;
        ChannelIterator iterator;
        Object objHasNext;
        ReceiveChannel receiveChannel;
        Comparator comparator2;
        Object $result;
        Comparator comparator3;
        ReceiveChannel $this$consume$iv3;
        ChannelIterator iterator2;
        Throwable th;
        Object max;
        Object max2;
        if (continuation instanceof C11601) {
            c11601 = (C11601) continuation;
            if ((c11601.label & Integer.MIN_VALUE) != 0) {
                c11601.label -= Integer.MIN_VALUE;
            } else {
                c11601 = new C11601(continuation);
            }
        }
        C11601 c116012 = c11601;
        Object max3 = c116012.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c116012.label) {
            case 0:
                ResultKt.throwOnFailure(max3);
                cause$iv = null;
                try {
                    iterator = $this$consume$iv.iterator();
                    c116012.L$0 = comparator;
                    c116012.L$1 = $this$consume$iv;
                    c116012.L$2 = iterator;
                    c116012.label = 1;
                    objHasNext = iterator.hasNext(c116012);
                    if (objHasNext == $result2) {
                        return $result2;
                    }
                    receiveChannel = $this$consume$iv;
                    comparator2 = comparator;
                    try {
                        if (((Boolean) objHasNext).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel, cause$iv);
                            return null;
                        }
                        ReceiveChannel $this$consume$iv4 = receiveChannel;
                        try {
                            Throwable cause$iv2 = cause$iv;
                            Object max4 = iterator.next();
                            ReceiveChannel $this$consume$iv5 = $this$consume$iv4;
                            try {
                                c116012.L$0 = comparator2;
                                c116012.L$1 = $this$consume$iv5;
                                c116012.L$2 = iterator;
                                c116012.L$3 = max4;
                                c116012.label = 2;
                                Object objHasNext2 = iterator.hasNext(c116012);
                                if (objHasNext2 != $result2) {
                                    return $result2;
                                }
                                Object obj = $result2;
                                $result = max3;
                                max3 = objHasNext2;
                                comparator3 = comparator2;
                                $this$consume$iv3 = $this$consume$iv5;
                                iterator2 = iterator;
                                th = cause$iv2;
                                max = max4;
                                max2 = obj;
                                try {
                                    if (((Boolean) max3).booleanValue()) {
                                        ChannelsKt.cancelConsumed($this$consume$iv3, th);
                                        return max;
                                    }
                                    Object e = iterator2.next();
                                    if (comparator3.compare(max, e) < 0) {
                                        ReceiveChannel $this$consume$iv6 = $this$consume$iv3;
                                        comparator2 = comparator3;
                                        Object obj2 = max2;
                                        max4 = e;
                                        max3 = $result;
                                        $result2 = obj2;
                                        ChannelIterator channelIterator = iterator2;
                                        $this$consume$iv5 = $this$consume$iv6;
                                        cause$iv2 = th;
                                        iterator = channelIterator;
                                    } else {
                                        ReceiveChannel $this$consume$iv7 = $this$consume$iv3;
                                        comparator2 = comparator3;
                                        ChannelIterator channelIterator2 = iterator2;
                                        $this$consume$iv5 = $this$consume$iv7;
                                        max3 = $result;
                                        $result2 = max2;
                                        max4 = max;
                                        cause$iv2 = th;
                                        iterator = channelIterator2;
                                    }
                                    c116012.L$0 = comparator2;
                                    c116012.L$1 = $this$consume$iv5;
                                    c116012.L$2 = iterator;
                                    c116012.L$3 = max4;
                                    c116012.label = 2;
                                    Object objHasNext22 = iterator.hasNext(c116012);
                                    if (objHasNext22 != $result2) {
                                    }
                                } catch (Throwable th2) {
                                    e$iv = th2;
                                    $this$consume$iv2 = $this$consume$iv3;
                                    Throwable cause$iv3 = e$iv;
                                    try {
                                        throw e$iv;
                                    } catch (Throwable e$iv2) {
                                        ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv3);
                                        throw e$iv2;
                                    }
                                }
                            } catch (Throwable th3) {
                                e$iv = th3;
                                $this$consume$iv2 = $this$consume$iv5;
                                Throwable cause$iv32 = e$iv;
                                throw e$iv;
                            }
                        } catch (Throwable th4) {
                            e$iv = th4;
                            $this$consume$iv2 = $this$consume$iv4;
                            Throwable cause$iv322 = e$iv;
                            throw e$iv;
                        }
                    } catch (Throwable th5) {
                        e$iv = th5;
                        $this$consume$iv2 = receiveChannel;
                    }
                } catch (Throwable th6) {
                    $this$consume$iv2 = $this$consume$iv;
                    e$iv = th6;
                    Throwable cause$iv3222 = e$iv;
                    throw e$iv;
                }
            case 1:
                ChannelIterator iterator3 = (ChannelIterator) c116012.L$2;
                ReceiveChannel receiveChannel2 = (ReceiveChannel) c116012.L$1;
                Comparator comparator4 = (Comparator) c116012.L$0;
                try {
                    ResultKt.throwOnFailure(max3);
                    objHasNext = max3;
                    comparator2 = comparator4;
                    receiveChannel = receiveChannel2;
                    iterator = iterator3;
                    cause$iv = null;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                } catch (Throwable th7) {
                    e$iv = th7;
                    $this$consume$iv2 = receiveChannel2;
                    Throwable cause$iv32222 = e$iv;
                    throw e$iv;
                }
                break;
            case 2:
                Object max5 = c116012.L$3;
                ChannelIterator iterator4 = (ChannelIterator) c116012.L$2;
                ReceiveChannel receiveChannel3 = (ReceiveChannel) c116012.L$1;
                Comparator comparator5 = (Comparator) c116012.L$0;
                try {
                    ResultKt.throwOnFailure(max3);
                    comparator3 = comparator5;
                    $this$consume$iv3 = receiveChannel3;
                    iterator2 = iterator4;
                    th = null;
                    max = max5;
                    max2 = $result2;
                    $result = max3;
                    if (((Boolean) max3).booleanValue()) {
                    }
                } catch (Throwable th8) {
                    e$iv = th8;
                    $this$consume$iv2 = receiveChannel3;
                    Throwable cause$iv322222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b5 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c7 A[Catch: all -> 0x00eb, TRY_LEAVE, TryCatch #6 {all -> 0x00eb, blocks: (B:38:0x00bf, B:40:0x00c7), top: B:73:0x00bf }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:37:0x00b6 -> B:73:0x00bf). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object minWith(ReceiveChannel $this$consume$iv, Comparator comparator, Continuation continuation) {
        C11611 c11611;
        Throwable cause$iv;
        ReceiveChannel $this$consume$iv2;
        Throwable e$iv;
        ChannelIterator iterator;
        Object objHasNext;
        ReceiveChannel receiveChannel;
        Comparator comparator2;
        Object $result;
        Comparator comparator3;
        ReceiveChannel $this$consume$iv3;
        ChannelIterator iterator2;
        Throwable th;
        Object min;
        Object min2;
        if (continuation instanceof C11611) {
            c11611 = (C11611) continuation;
            if ((c11611.label & Integer.MIN_VALUE) != 0) {
                c11611.label -= Integer.MIN_VALUE;
            } else {
                c11611 = new C11611(continuation);
            }
        }
        C11611 c116112 = c11611;
        Object min3 = c116112.result;
        Object $result2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c116112.label) {
            case 0:
                ResultKt.throwOnFailure(min3);
                cause$iv = null;
                try {
                    iterator = $this$consume$iv.iterator();
                    c116112.L$0 = comparator;
                    c116112.L$1 = $this$consume$iv;
                    c116112.L$2 = iterator;
                    c116112.label = 1;
                    objHasNext = iterator.hasNext(c116112);
                    if (objHasNext == $result2) {
                        return $result2;
                    }
                    receiveChannel = $this$consume$iv;
                    comparator2 = comparator;
                    try {
                        if (((Boolean) objHasNext).booleanValue()) {
                            ChannelsKt.cancelConsumed(receiveChannel, cause$iv);
                            return null;
                        }
                        ReceiveChannel $this$consume$iv4 = receiveChannel;
                        try {
                            Throwable cause$iv2 = cause$iv;
                            Object min4 = iterator.next();
                            ReceiveChannel $this$consume$iv5 = $this$consume$iv4;
                            try {
                                c116112.L$0 = comparator2;
                                c116112.L$1 = $this$consume$iv5;
                                c116112.L$2 = iterator;
                                c116112.L$3 = min4;
                                c116112.label = 2;
                                Object objHasNext2 = iterator.hasNext(c116112);
                                if (objHasNext2 != $result2) {
                                    return $result2;
                                }
                                Object obj = $result2;
                                $result = min3;
                                min3 = objHasNext2;
                                comparator3 = comparator2;
                                $this$consume$iv3 = $this$consume$iv5;
                                iterator2 = iterator;
                                th = cause$iv2;
                                min = min4;
                                min2 = obj;
                                try {
                                    if (((Boolean) min3).booleanValue()) {
                                        ChannelsKt.cancelConsumed($this$consume$iv3, th);
                                        return min;
                                    }
                                    Object e = iterator2.next();
                                    if (comparator3.compare(min, e) > 0) {
                                        ReceiveChannel $this$consume$iv6 = $this$consume$iv3;
                                        comparator2 = comparator3;
                                        Object obj2 = min2;
                                        min4 = e;
                                        min3 = $result;
                                        $result2 = obj2;
                                        ChannelIterator channelIterator = iterator2;
                                        $this$consume$iv5 = $this$consume$iv6;
                                        cause$iv2 = th;
                                        iterator = channelIterator;
                                    } else {
                                        ReceiveChannel $this$consume$iv7 = $this$consume$iv3;
                                        comparator2 = comparator3;
                                        ChannelIterator channelIterator2 = iterator2;
                                        $this$consume$iv5 = $this$consume$iv7;
                                        min3 = $result;
                                        $result2 = min2;
                                        min4 = min;
                                        cause$iv2 = th;
                                        iterator = channelIterator2;
                                    }
                                    c116112.L$0 = comparator2;
                                    c116112.L$1 = $this$consume$iv5;
                                    c116112.L$2 = iterator;
                                    c116112.L$3 = min4;
                                    c116112.label = 2;
                                    Object objHasNext22 = iterator.hasNext(c116112);
                                    if (objHasNext22 != $result2) {
                                    }
                                } catch (Throwable th2) {
                                    e$iv = th2;
                                    $this$consume$iv2 = $this$consume$iv3;
                                    Throwable cause$iv3 = e$iv;
                                    try {
                                        throw e$iv;
                                    } catch (Throwable e$iv2) {
                                        ChannelsKt.cancelConsumed($this$consume$iv2, cause$iv3);
                                        throw e$iv2;
                                    }
                                }
                            } catch (Throwable th3) {
                                e$iv = th3;
                                $this$consume$iv2 = $this$consume$iv5;
                                Throwable cause$iv32 = e$iv;
                                throw e$iv;
                            }
                        } catch (Throwable th4) {
                            e$iv = th4;
                            $this$consume$iv2 = $this$consume$iv4;
                            Throwable cause$iv322 = e$iv;
                            throw e$iv;
                        }
                    } catch (Throwable th5) {
                        e$iv = th5;
                        $this$consume$iv2 = receiveChannel;
                    }
                } catch (Throwable th6) {
                    $this$consume$iv2 = $this$consume$iv;
                    e$iv = th6;
                    Throwable cause$iv3222 = e$iv;
                    throw e$iv;
                }
            case 1:
                ChannelIterator iterator3 = (ChannelIterator) c116112.L$2;
                ReceiveChannel receiveChannel2 = (ReceiveChannel) c116112.L$1;
                Comparator comparator4 = (Comparator) c116112.L$0;
                try {
                    ResultKt.throwOnFailure(min3);
                    objHasNext = min3;
                    comparator2 = comparator4;
                    receiveChannel = receiveChannel2;
                    iterator = iterator3;
                    cause$iv = null;
                    if (((Boolean) objHasNext).booleanValue()) {
                    }
                } catch (Throwable th7) {
                    e$iv = th7;
                    $this$consume$iv2 = receiveChannel2;
                    Throwable cause$iv32222 = e$iv;
                    throw e$iv;
                }
                break;
            case 2:
                Object min5 = c116112.L$3;
                ChannelIterator iterator4 = (ChannelIterator) c116112.L$2;
                ReceiveChannel receiveChannel3 = (ReceiveChannel) c116112.L$1;
                Comparator comparator5 = (Comparator) c116112.L$0;
                try {
                    ResultKt.throwOnFailure(min3);
                    comparator3 = comparator5;
                    $this$consume$iv3 = receiveChannel3;
                    iterator2 = iterator4;
                    th = null;
                    min = min5;
                    min2 = $result2;
                    $result = min3;
                    if (((Boolean) min3).booleanValue()) {
                    }
                } catch (Throwable th8) {
                    e$iv = th8;
                    $this$consume$iv2 = receiveChannel3;
                    Throwable cause$iv322222 = e$iv;
                    throw e$iv;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object none(ReceiveChannel $this$none, Continuation continuation) {
        C11621 c11621;
        ReceiveChannel $this$consume$iv;
        Throwable cause$iv;
        Object objHasNext;
        if (continuation instanceof C11621) {
            c11621 = (C11621) continuation;
            if ((c11621.label & Integer.MIN_VALUE) != 0) {
                c11621.label -= Integer.MIN_VALUE;
            } else {
                c11621 = new C11621(continuation);
            }
        }
        C11621 c116212 = c11621;
        Object $result = c116212.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        boolean z = true;
        switch (c116212.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                $this$consume$iv = $this$none;
                cause$iv = null;
                try {
                    ChannelIterator it = $this$consume$iv.iterator();
                    c116212.L$0 = $this$consume$iv;
                    c116212.label = 1;
                    objHasNext = it.hasNext(c116212);
                    if (objHasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (!((Boolean) objHasNext).booleanValue()) {
                        z = false;
                    }
                    Boolean boolBoxBoolean = Boxing.boxBoolean(z);
                    ChannelsKt.cancelConsumed($this$consume$iv, cause$iv);
                    return boolBoxBoolean;
                } catch (Throwable th) {
                    e$iv = th;
                    Throwable cause$iv2 = e$iv;
                    try {
                        throw e$iv;
                    } catch (Throwable e$iv) {
                        ChannelsKt.cancelConsumed($this$consume$iv, cause$iv2);
                        throw e$iv;
                    }
                }
            case 1:
                $this$consume$iv = (ReceiveChannel) c116212.L$0;
                cause$iv = null;
                try {
                    ResultKt.throwOnFailure($result);
                    objHasNext = $result;
                    if (!((Boolean) objHasNext).booleanValue()) {
                    }
                    Boolean boolBoxBoolean2 = Boxing.boxBoolean(z);
                    ChannelsKt.cancelConsumed($this$consume$iv, cause$iv);
                    return boolBoxBoolean2;
                } catch (Throwable th2) {
                    e$iv = th2;
                    Throwable cause$iv22 = e$iv;
                    throw e$iv;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\n\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\u0010\u0000\u001a\u0002H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u0001H\u0001H\u008a@"}, m146d2 = {"<anonymous>", "E", "", "it"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$requireNoNulls$1", m162f = "Deprecated.kt", m163i = {}, m164l = {}, m165m = "invokeSuspend", m166n = {}, m167s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$requireNoNulls$1 */
    static final class C11631<E> extends SuspendLambda implements Function2<E, Continuation<? super E>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_requireNoNulls;
        /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11631(ReceiveChannel<? extends E> receiveChannel, Continuation<? super C11631> continuation) {
            super(2, continuation);
            this.$this_requireNoNulls = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11631 c11631 = new C11631(this.$this_requireNoNulls, continuation);
            c11631.L$0 = obj;
            return c11631;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
            return invoke((C11631<E>) obj, (Continuation<? super C11631<E>>) obj2);
        }

        public final Object invoke(E e, Continuation<? super E> continuation) {
            return ((C11631) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure(obj);
                    Object it = this.L$0;
                    if (it != null) {
                        return it;
                    }
                    throw new IllegalArgumentException("null element found in " + this.$this_requireNoNulls + '.');
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static /* synthetic */ ReceiveChannel zip$default(ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.zip(receiveChannel, receiveChannel2, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [V] */
    /* compiled from: Deprecated.kt */
    @Metadata(m145d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005H\u008a@"}, m146d2 = {"<anonymous>", "", "E", "R", "V", "Lkotlinx/coroutines/channels/ProducerScope;"}, m147k = 3, m148mv = {1, 9, 0}, m150xi = 48)
    @DebugMetadata(m161c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$zip$2", m162f = "Deprecated.kt", m163i = {0, 0, 0, 1, 1, 1, 1, 2, 2, 2}, m164l = {517, 499, TypedValues.PositionType.TYPE_TRANSITION_EASING}, m165m = "invokeSuspend", m166n = {"$this$produce", "otherIterator", "$this$consume$iv$iv", "$this$produce", "otherIterator", "$this$consume$iv$iv", "element1", "$this$produce", "otherIterator", "$this$consume$iv$iv"}, m167s = {"L$0", "L$1", "L$3", "L$0", "L$1", "L$3", "L$5", "L$0", "L$1", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$zip$2 */
    static final class C11732<V> extends SuspendLambda implements Function2<ProducerScope<? super V>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<R> $other;
        final /* synthetic */ ReceiveChannel<E> $this_zip;
        final /* synthetic */ Function2<E, R, V> $transform;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C11732(ReceiveChannel<? extends R> receiveChannel, ReceiveChannel<? extends E> receiveChannel2, Function2<? super E, ? super R, ? extends V> function2, Continuation<? super C11732> continuation) {
            super(2, continuation);
            this.$other = receiveChannel;
            this.$this_zip = receiveChannel2;
            this.$transform = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C11732 c11732 = new C11732(this.$other, this.$this_zip, this.$transform, continuation);
            c11732.L$0 = obj;
            return c11732;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super V> producerScope, Continuation<? super Unit> continuation) {
            return ((C11732) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:24:0x00d3 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00d4  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00ea A[Catch: all -> 0x0172, TRY_LEAVE, TryCatch #0 {all -> 0x0172, blocks: (B:26:0x00e2, B:28:0x00ea, B:47:0x0167), top: B:59:0x00e2 }] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0114  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x014c  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x0167 A[Catch: all -> 0x0172, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0172, blocks: (B:26:0x00e2, B:28:0x00ea, B:47:0x0167), top: B:59:0x00e2 }] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x0136 -> B:39:0x0140). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:42:0x014c -> B:43:0x015a). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) {
            ReceiveChannel $this$consume$iv$iv;
            C11732 c11732;
            Object $result;
            Throwable cause$iv$iv;
            ChannelIterator otherIterator;
            ChannelIterator otherIterator2;
            int $i$f$consume;
            ProducerScope $this$produce;
            Object $result2;
            Function2 function2;
            ChannelIterator it;
            Object $result3;
            ProducerScope $this$produce2;
            ChannelIterator otherIterator3;
            ReceiveChannel $this$consume$iv$iv2;
            Throwable cause$iv$iv2;
            Function2 function22;
            ChannelIterator channelIterator;
            int i;
            ChannelIterator channelIterator2;
            Object obj2;
            Object $result4;
            Object element1;
            Object element12;
            ChannelIterator channelIterator3;
            int i2;
            Function2 function23;
            ChannelIterator channelIterator4;
            Function2 function24;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            Object obj3 = null;
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure(obj);
                        c11732 = this;
                        $result = obj;
                        ProducerScope $this$produce3 = (ProducerScope) c11732.L$0;
                        ChannelIterator otherIterator4 = c11732.$other.iterator();
                        ReceiveChannel $this$consumeEach$iv = c11732.$this_zip;
                        $this$consume$iv$iv = $this$consumeEach$iv;
                        cause$iv$iv = null;
                        try {
                            otherIterator = otherIterator4;
                            otherIterator2 = null;
                            $i$f$consume = 0;
                            $this$produce = $this$produce3;
                            $result2 = null;
                            function2 = c11732.$transform;
                            it = $this$consume$iv$iv.iterator();
                            c11732.L$0 = $this$produce;
                            c11732.L$1 = otherIterator;
                            c11732.L$2 = function2;
                            c11732.L$3 = $this$consume$iv$iv;
                            c11732.L$4 = it;
                            c11732.L$5 = obj3;
                            c11732.label = 1;
                            objHasNext = it.hasNext(c11732);
                            if (objHasNext != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            Object obj4 = $result2;
                            $result4 = $result;
                            $result3 = objHasNext;
                            $this$produce2 = $this$produce;
                            otherIterator3 = otherIterator;
                            $this$consume$iv$iv2 = $this$consume$iv$iv;
                            cause$iv$iv2 = cause$iv$iv;
                            function22 = function2;
                            channelIterator = it;
                            i = $i$f$consume;
                            channelIterator2 = otherIterator2;
                            obj2 = obj4;
                            try {
                                if (((Boolean) $result3).booleanValue()) {
                                    Unit unit = Unit.INSTANCE;
                                    ChannelsKt.cancelConsumed($this$consume$iv$iv2, cause$iv$iv2);
                                    return Unit.INSTANCE;
                                }
                                Object element13 = channelIterator.next();
                                i2 = 0;
                                c11732.L$0 = $this$produce2;
                                c11732.L$1 = otherIterator3;
                                c11732.L$2 = function22;
                                c11732.L$3 = $this$consume$iv$iv2;
                                c11732.L$4 = channelIterator;
                                c11732.L$5 = element13;
                                c11732.label = 2;
                                Object objHasNext2 = otherIterator3.hasNext(c11732);
                                if (objHasNext2 == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                element1 = objHasNext2;
                                function23 = function22;
                                channelIterator3 = channelIterator;
                                element12 = element13;
                                try {
                                    if (((Boolean) element1).booleanValue()) {
                                        Throwable th = cause$iv$iv2;
                                        function2 = function23;
                                        $result = $result4;
                                        $result2 = obj2;
                                        otherIterator2 = channelIterator2;
                                        $i$f$consume = i;
                                        it = channelIterator3;
                                        $this$consume$iv$iv = $this$consume$iv$iv2;
                                        otherIterator = otherIterator3;
                                        $this$produce = $this$produce2;
                                        cause$iv$iv = th;
                                        obj3 = null;
                                        c11732.L$0 = $this$produce;
                                        c11732.L$1 = otherIterator;
                                        c11732.L$2 = function2;
                                        c11732.L$3 = $this$consume$iv$iv;
                                        c11732.L$4 = it;
                                        c11732.L$5 = obj3;
                                        c11732.label = 1;
                                        objHasNext = it.hasNext(c11732);
                                        if (objHasNext != coroutine_suspended) {
                                        }
                                    } else {
                                        Throwable cause$iv$iv3 = cause$iv$iv2;
                                        try {
                                            Object element2 = otherIterator3.next();
                                            Object objInvoke = function23.invoke(element12, element2);
                                            c11732.L$0 = $this$produce2;
                                            c11732.L$1 = otherIterator3;
                                            c11732.L$2 = function23;
                                            c11732.L$3 = $this$consume$iv$iv2;
                                            c11732.L$4 = channelIterator3;
                                            Function2 function25 = function23;
                                            c11732.L$5 = null;
                                            c11732.label = 3;
                                            if ($this$produce2.send(objInvoke, c11732) == coroutine_suspended) {
                                                return coroutine_suspended;
                                            }
                                            channelIterator4 = channelIterator3;
                                            $this$consume$iv$iv = $this$consume$iv$iv2;
                                            function24 = function25;
                                            cause$iv$iv = cause$iv$iv3;
                                            $result = $result4;
                                            $result2 = obj2;
                                            otherIterator2 = channelIterator2;
                                            $i$f$consume = i;
                                            it = channelIterator4;
                                            function2 = function24;
                                            otherIterator = otherIterator3;
                                            $this$produce = $this$produce2;
                                            obj3 = null;
                                            c11732.L$0 = $this$produce;
                                            c11732.L$1 = otherIterator;
                                            c11732.L$2 = function2;
                                            c11732.L$3 = $this$consume$iv$iv;
                                            c11732.L$4 = it;
                                            c11732.L$5 = obj3;
                                            c11732.label = 1;
                                            objHasNext = it.hasNext(c11732);
                                            if (objHasNext != coroutine_suspended) {
                                            }
                                        } catch (Throwable th2) {
                                            e$iv$iv = th2;
                                            $this$consume$iv$iv = $this$consume$iv$iv2;
                                            Throwable cause$iv$iv4 = e$iv$iv;
                                            try {
                                                throw e$iv$iv;
                                            } catch (Throwable e$iv$iv) {
                                                ChannelsKt.cancelConsumed($this$consume$iv$iv, cause$iv$iv4);
                                                throw e$iv$iv;
                                            }
                                        }
                                    }
                                } catch (Throwable th3) {
                                    e$iv$iv = th3;
                                    $this$consume$iv$iv = $this$consume$iv$iv2;
                                }
                            } catch (Throwable th4) {
                                e$iv$iv = th4;
                                $this$consume$iv$iv = $this$consume$iv$iv2;
                                Throwable cause$iv$iv42 = e$iv$iv;
                                throw e$iv$iv;
                            }
                        } catch (Throwable th5) {
                            e$iv$iv = th5;
                            Throwable cause$iv$iv422 = e$iv$iv;
                            throw e$iv$iv;
                        }
                    case 1:
                        c11732 = this;
                        $result3 = obj;
                        ChannelIterator channelIterator5 = (ChannelIterator) c11732.L$4;
                        ReceiveChannel $this$consume$iv$iv3 = (ReceiveChannel) c11732.L$3;
                        Function2 function26 = (Function2) c11732.L$2;
                        ChannelIterator otherIterator5 = (ChannelIterator) c11732.L$1;
                        ProducerScope $this$produce4 = (ProducerScope) c11732.L$0;
                        ResultKt.throwOnFailure($result3);
                        $this$produce2 = $this$produce4;
                        otherIterator3 = otherIterator5;
                        $this$consume$iv$iv2 = $this$consume$iv$iv3;
                        cause$iv$iv2 = null;
                        function22 = function26;
                        channelIterator = channelIterator5;
                        i = 0;
                        channelIterator2 = null;
                        obj2 = null;
                        $result4 = $result3;
                        if (((Boolean) $result3).booleanValue()) {
                        }
                        break;
                    case 2:
                        c11732 = this;
                        element1 = obj;
                        element12 = c11732.L$5;
                        channelIterator3 = (ChannelIterator) c11732.L$4;
                        $this$consume$iv$iv = (ReceiveChannel) c11732.L$3;
                        Function2 function27 = (Function2) c11732.L$2;
                        otherIterator3 = (ChannelIterator) c11732.L$1;
                        $this$produce2 = (ProducerScope) c11732.L$0;
                        try {
                            ResultKt.throwOnFailure(element1);
                            i2 = 0;
                            i = 0;
                            channelIterator2 = null;
                            obj2 = null;
                            $result4 = element1;
                            cause$iv$iv2 = null;
                            function23 = function27;
                            $this$consume$iv$iv2 = $this$consume$iv$iv;
                            if (((Boolean) element1).booleanValue()) {
                            }
                        } catch (Throwable th6) {
                            e$iv$iv = th6;
                            Throwable cause$iv$iv4222 = e$iv$iv;
                            throw e$iv$iv;
                        }
                        break;
                    case 3:
                        c11732 = this;
                        $result = obj;
                        $result2 = null;
                        otherIterator2 = null;
                        $i$f$consume = 0;
                        channelIterator4 = (ChannelIterator) c11732.L$4;
                        cause$iv$iv = null;
                        $this$consume$iv$iv = (ReceiveChannel) c11732.L$3;
                        function24 = (Function2) c11732.L$2;
                        otherIterator3 = (ChannelIterator) c11732.L$1;
                        $this$produce2 = (ProducerScope) c11732.L$0;
                        ResultKt.throwOnFailure($result);
                        it = channelIterator4;
                        function2 = function24;
                        otherIterator = otherIterator3;
                        $this$produce = $this$produce2;
                        obj3 = null;
                        c11732.L$0 = $this$produce;
                        c11732.L$1 = otherIterator;
                        c11732.L$2 = function2;
                        c11732.L$3 = $this$consume$iv$iv;
                        c11732.L$4 = it;
                        c11732.L$5 = obj3;
                        c11732.label = 1;
                        objHasNext = it.hasNext(c11732);
                        if (objHasNext != coroutine_suspended) {
                        }
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            } catch (Throwable th7) {
                e$iv$iv = th7;
            }
        }
    }

    public static final <E, R, V> ReceiveChannel<V> zip(ReceiveChannel<? extends E> receiveChannel, ReceiveChannel<? extends R> receiveChannel2, CoroutineContext context, Function2<? super E, ? super R, ? extends V> function2) {
        return ProduceKt.produce(GlobalScope.INSTANCE, (6 & 1) != 0 ? EmptyCoroutineContext.INSTANCE : context, (6 & 2) != 0 ? 0 : 0, (6 & 4) != 0 ? CoroutineStart.DEFAULT : null, (6 & 8) != 0 ? null : ChannelsKt.consumesAll(receiveChannel, receiveChannel2), new C11732(receiveChannel2, receiveChannel, function2, null));
    }

    public static final Function1<Throwable, Unit> consumes(final ReceiveChannel<?> receiveChannel) {
        return new Function1<Throwable, Unit>() { // from class: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.consumes.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(Throwable cause) {
                ChannelsKt.cancelConsumed(receiveChannel, cause);
            }
        };
    }
}
