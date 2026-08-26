$target = "C:/Users/User/AndroidStudioProjects/Lxquiz/core/src/main/java/com/example/lxquiz"
New-Item -ItemType Directory -Force $target

$dirs = @("core", "data", "di", "domain", "features", "navigation", "ui")
foreach ($dir in $dirs) {
    $src = "C:/Users/User/AndroidStudioProjects/Lxquiz/app/src/main/java/com/example/lxquiz/" + $dir
    if (Test-Path $src) {
        Write-Copying $src to $target
        Copy-Item -Recurse -Force $src $target
        Remove-Item -Recurse -Force $src
    }
}

$graphqlSrc = "C:/Users/User/AndroidStudioProjects/Lxquiz/app/src/main/graphql"
if (Test-Path $graphqlSrc) {
    $coreMain = "C:/Users/User/AndroidStudioProjects/Lxquiz/core/src/main"
    New-Item -ItemType Directory -Force $coreMain
    Copy-Item -Recurse -Force $graphqlSrc $coreMain
    Remove-Item -Recurse -Force $graphqlSrc
}
