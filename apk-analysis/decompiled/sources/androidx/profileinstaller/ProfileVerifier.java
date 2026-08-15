package androidx.profileinstaller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.concurrent.futures.ResolvableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/* loaded from: classes.dex */
public final class ProfileVerifier {
    private static final String CUR_PROFILES_BASE_DIR = "/data/misc/profiles/cur/0/";
    private static final String PROFILE_FILE_NAME = "primary.prof";
    private static final String PROFILE_INSTALLED_CACHE_FILE_NAME = "profileInstalled";
    private static final String REF_PROFILES_BASE_DIR = "/data/misc/profiles/ref/";
    private static final String TAG = "ProfileVerifier";
    private static final ResolvableFuture<CompilationStatus> sFuture = ResolvableFuture.create();
    private static final Object SYNC_OBJ = new Object();
    private static CompilationStatus sCompilationStatus = null;

    private ProfileVerifier() {
    }

    public static CompilationStatus writeProfileVerification(Context context) {
        return writeProfileVerification(context, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x009e A[Catch: all -> 0x0116, TryCatch #1 {, blocks: (B:10:0x000e, B:12:0x0012, B:13:0x0014, B:15:0x0016, B:17:0x001d, B:20:0x0025, B:26:0x004c, B:31:0x0070, B:32:0x0075, B:34:0x0088, B:44:0x009e, B:46:0x00a6, B:49:0x00ab, B:62:0x00c3, B:66:0x00ca, B:71:0x00d5, B:73:0x00e9, B:80:0x00fa, B:81:0x00fe, B:75:0x00ef, B:38:0x0094, B:39:0x0098, B:84:0x0101, B:85:0x010c, B:87:0x010e, B:88:0x0114), top: B:95:0x000e, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00bb A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00c3 A[Catch: all -> 0x0116, TryCatch #1 {, blocks: (B:10:0x000e, B:12:0x0012, B:13:0x0014, B:15:0x0016, B:17:0x001d, B:20:0x0025, B:26:0x004c, B:31:0x0070, B:32:0x0075, B:34:0x0088, B:44:0x009e, B:46:0x00a6, B:49:0x00ab, B:62:0x00c3, B:66:0x00ca, B:71:0x00d5, B:73:0x00e9, B:80:0x00fa, B:81:0x00fe, B:75:0x00ef, B:38:0x0094, B:39:0x0098, B:84:0x0101, B:85:0x010c, B:87:0x010e, B:88:0x0114), top: B:95:0x000e, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00e9 A[Catch: all -> 0x0116, TRY_LEAVE, TryCatch #1 {, blocks: (B:10:0x000e, B:12:0x0012, B:13:0x0014, B:15:0x0016, B:17:0x001d, B:20:0x0025, B:26:0x004c, B:31:0x0070, B:32:0x0075, B:34:0x0088, B:44:0x009e, B:46:0x00a6, B:49:0x00ab, B:62:0x00c3, B:66:0x00ca, B:71:0x00d5, B:73:0x00e9, B:80:0x00fa, B:81:0x00fe, B:75:0x00ef, B:38:0x0094, B:39:0x0098, B:84:0x0101, B:85:0x010c, B:87:0x010e, B:88:0x0114), top: B:95:0x000e, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x00ef A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0088 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static CompilationStatus writeProfileVerification(Context context, boolean forceVerifyCurrentProfile) {
        boolean hasCurrentProfile;
        File currentProfileFile;
        long currentProfileSize;
        File cacheFile;
        Cache currentCache;
        boolean hasReferenceProfile;
        int resultCode;
        int resultCode2;
        int resultCode3;
        Cache newCache;
        Cache currentCache2;
        if (!forceVerifyCurrentProfile && sCompilationStatus != null) {
            return sCompilationStatus;
        }
        synchronized (SYNC_OBJ) {
            if (!forceVerifyCurrentProfile) {
                if (sCompilationStatus != null) {
                    return sCompilationStatus;
                }
                hasCurrentProfile = false;
                if (Build.VERSION.SDK_INT >= 28 && Build.VERSION.SDK_INT != 30) {
                    File referenceProfileFile = new File(new File(REF_PROFILES_BASE_DIR, context.getPackageName()), PROFILE_FILE_NAME);
                    long referenceProfileSize = referenceProfileFile.length();
                    boolean hasReferenceProfile2 = !referenceProfileFile.exists() && referenceProfileSize > 0;
                    currentProfileFile = new File(new File(CUR_PROFILES_BASE_DIR, context.getPackageName()), PROFILE_FILE_NAME);
                    currentProfileSize = currentProfileFile.length();
                    if (currentProfileFile.exists() && currentProfileSize > 0) {
                        hasCurrentProfile = true;
                    }
                    try {
                        long packageLastUpdateTime = getPackageLastUpdateTime(context);
                        cacheFile = new File(context.getFilesDir(), PROFILE_INSTALLED_CACHE_FILE_NAME);
                        if (cacheFile.exists()) {
                            currentCache = null;
                        } else {
                            try {
                                Cache currentCache3 = Cache.readFromFile(cacheFile);
                                currentCache = currentCache3;
                            } catch (IOException e) {
                                return setCompilationStatus(131072, hasReferenceProfile2, hasCurrentProfile);
                            }
                        }
                        if (currentCache == null) {
                            hasReferenceProfile = hasReferenceProfile2;
                            if (currentCache.mPackageLastUpdateTime == packageLastUpdateTime && currentCache.mResultCode != 2) {
                                resultCode = currentCache.mResultCode;
                            }
                            if (forceVerifyCurrentProfile && hasCurrentProfile && resultCode != 1) {
                                resultCode = 2;
                            }
                            if (currentCache == null || currentCache.mResultCode != 2 || resultCode != 1) {
                                resultCode2 = resultCode;
                            } else {
                                resultCode2 = resultCode;
                                if (referenceProfileSize < currentCache.mInstalledCurrentProfileSize) {
                                    resultCode3 = 3;
                                }
                                currentCache2 = currentCache;
                                newCache = new Cache(1, resultCode3, packageLastUpdateTime, currentProfileSize);
                                if (currentCache2 != null || !currentCache2.equals(newCache)) {
                                    try {
                                        newCache.writeOnFile(cacheFile);
                                    } catch (IOException e2) {
                                        resultCode3 = CompilationStatus.f95xf2722a21;
                                    }
                                }
                                return setCompilationStatus(resultCode3, hasReferenceProfile, hasCurrentProfile);
                            }
                            resultCode3 = resultCode2;
                            currentCache2 = currentCache;
                            newCache = new Cache(1, resultCode3, packageLastUpdateTime, currentProfileSize);
                            if (currentCache2 != null) {
                                newCache.writeOnFile(cacheFile);
                            }
                            return setCompilationStatus(resultCode3, hasReferenceProfile, hasCurrentProfile);
                        }
                        hasReferenceProfile = hasReferenceProfile2;
                        if (!hasReferenceProfile) {
                            resultCode = 1;
                        } else if (hasCurrentProfile) {
                            resultCode = 2;
                        } else {
                            resultCode = 0;
                        }
                        if (forceVerifyCurrentProfile) {
                            resultCode = 2;
                        }
                        if (currentCache == null) {
                            resultCode2 = resultCode;
                            resultCode3 = resultCode2;
                        }
                        currentCache2 = currentCache;
                        newCache = new Cache(1, resultCode3, packageLastUpdateTime, currentProfileSize);
                        if (currentCache2 != null) {
                        }
                        return setCompilationStatus(resultCode3, hasReferenceProfile, hasCurrentProfile);
                    } catch (PackageManager.NameNotFoundException e3) {
                        return setCompilationStatus(65536, hasReferenceProfile2, hasCurrentProfile);
                    }
                }
                return setCompilationStatus(262144, false, false);
            }
            hasCurrentProfile = false;
            if (Build.VERSION.SDK_INT >= 28) {
                File referenceProfileFile2 = new File(new File(REF_PROFILES_BASE_DIR, context.getPackageName()), PROFILE_FILE_NAME);
                long referenceProfileSize2 = referenceProfileFile2.length();
                if (referenceProfileFile2.exists()) {
                    boolean hasReferenceProfile22 = !referenceProfileFile2.exists() && referenceProfileSize2 > 0;
                    currentProfileFile = new File(new File(CUR_PROFILES_BASE_DIR, context.getPackageName()), PROFILE_FILE_NAME);
                    currentProfileSize = currentProfileFile.length();
                    if (currentProfileFile.exists()) {
                        hasCurrentProfile = true;
                    }
                    long packageLastUpdateTime2 = getPackageLastUpdateTime(context);
                    cacheFile = new File(context.getFilesDir(), PROFILE_INSTALLED_CACHE_FILE_NAME);
                    if (cacheFile.exists()) {
                    }
                    if (currentCache == null) {
                    }
                    if (!hasReferenceProfile) {
                    }
                    if (forceVerifyCurrentProfile) {
                    }
                    if (currentCache == null) {
                    }
                    currentCache2 = currentCache;
                    newCache = new Cache(1, resultCode3, packageLastUpdateTime2, currentProfileSize);
                    if (currentCache2 != null) {
                    }
                    return setCompilationStatus(resultCode3, hasReferenceProfile, hasCurrentProfile);
                }
            }
            return setCompilationStatus(262144, false, false);
        }
    }

    private static CompilationStatus setCompilationStatus(int resultCode, boolean hasReferenceProfile, boolean hasCurrentProfile) {
        sCompilationStatus = new CompilationStatus(resultCode, hasReferenceProfile, hasCurrentProfile);
        sFuture.set(sCompilationStatus);
        return sCompilationStatus;
    }

    private static long getPackageLastUpdateTime(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        if (Build.VERSION.SDK_INT >= 33) {
            return Api33Impl.getPackageInfo(packageManager, context).lastUpdateTime;
        }
        return packageManager.getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
    }

    public static ListenableFuture<CompilationStatus> getCompilationStatusAsync() {
        return sFuture;
    }

    static class Cache {
        private static final int SCHEMA = 1;
        final long mInstalledCurrentProfileSize;
        final long mPackageLastUpdateTime;
        final int mResultCode;
        final int mSchema;

        Cache(int schema, int resultCode, long packageLastUpdateTime, long installedCurrentProfileSize) {
            this.mSchema = schema;
            this.mResultCode = resultCode;
            this.mPackageLastUpdateTime = packageLastUpdateTime;
            this.mInstalledCurrentProfileSize = installedCurrentProfileSize;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof Cache)) {
                return false;
            }
            Cache cacheFile = (Cache) o;
            if (this.mResultCode == cacheFile.mResultCode && this.mPackageLastUpdateTime == cacheFile.mPackageLastUpdateTime && this.mSchema == cacheFile.mSchema && this.mInstalledCurrentProfileSize == cacheFile.mInstalledCurrentProfileSize) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mResultCode), Long.valueOf(this.mPackageLastUpdateTime), Integer.valueOf(this.mSchema), Long.valueOf(this.mInstalledCurrentProfileSize));
        }

        void writeOnFile(File file) throws IOException {
            file.delete();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            try {
                dos.writeInt(this.mSchema);
                dos.writeInt(this.mResultCode);
                dos.writeLong(this.mPackageLastUpdateTime);
                dos.writeLong(this.mInstalledCurrentProfileSize);
                dos.close();
            } catch (Throwable th) {
                try {
                    dos.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        static Cache readFromFile(File file) throws IOException {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            try {
                Cache cache = new Cache(dis.readInt(), dis.readInt(), dis.readLong(), dis.readLong());
                dis.close();
                return cache;
            } catch (Throwable th) {
                try {
                    dis.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    public static class CompilationStatus {
        public static final int RESULT_CODE_COMPILED_WITH_PROFILE = 1;
        public static final int RESULT_CODE_COMPILED_WITH_PROFILE_NON_MATCHING = 3;
        public static final int RESULT_CODE_ERROR_CACHE_FILE_EXISTS_BUT_CANNOT_BE_READ = 131072;

        /* renamed from: RESULT_CODE_ERROR_CANT_WRITE_PROFILE_VERIFICATION_RESULT_CACHE_FILE */
        public static final int f95xf2722a21 = 196608;
        private static final int RESULT_CODE_ERROR_CODE_BIT_SHIFT = 16;
        public static final int RESULT_CODE_ERROR_PACKAGE_NAME_DOES_NOT_EXIST = 65536;
        public static final int RESULT_CODE_ERROR_UNSUPPORTED_API_VERSION = 262144;
        public static final int RESULT_CODE_NO_PROFILE = 0;
        public static final int RESULT_CODE_PROFILE_ENQUEUED_FOR_COMPILATION = 2;
        private final boolean mHasCurrentProfile;
        private final boolean mHasReferenceProfile;
        final int mResultCode;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ResultCode {
        }

        CompilationStatus(int resultCode, boolean hasReferenceProfile, boolean hasCurrentProfile) {
            this.mResultCode = resultCode;
            this.mHasCurrentProfile = hasCurrentProfile;
            this.mHasReferenceProfile = hasReferenceProfile;
        }

        public int getProfileInstallResultCode() {
            return this.mResultCode;
        }

        public boolean isCompiledWithProfile() {
            return this.mHasReferenceProfile;
        }

        public boolean hasProfileEnqueuedForCompilation() {
            return this.mHasCurrentProfile;
        }
    }

    private static class Api33Impl {
        private Api33Impl() {
        }

        static PackageInfo getPackageInfo(PackageManager packageManager, Context context) throws PackageManager.NameNotFoundException {
            return packageManager.getPackageInfo(context.getPackageName(), PackageManager.PackageInfoFlags.of(0L));
        }
    }
}
