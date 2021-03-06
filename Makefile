## @author Manuel Gieseking

# dependencies (folders and repos should be equally ordered)
DEPENDENCIES_FOLDERS="libs,framework"
DEPENDENCIES_REPOS="https://github.com/adamtool/libs.git,https://github.com/adamtool/framework.git"
DEPENDENCIES_REV="HEAD,HEAD"
t=javac

# should be executed no matter if a file with the same name exists or not
.PHONY: check_dependencies
.PHONY: pull_dependencies
.PHONY: rm_dependencies
.PHONY: tools
.PHONY: ui
#.PHONY: javadoc
.PHONY: setJavac
.PHONY: setJar
.PHONY: setClean
.PHONY: setCleanAll
.PHONY: clean
.PHONY: clean-all
.PHONY: src_withlibs
.PHONY: src

define generate_src
	mkdir -p adam_src
	if [ $(1) = true ]; then\
		cp -R ./dependencies/libs ./adam_src/libs/; \
		rm -rf ./adam_src/libs/.git; \
	fi
	for i in $$(find . -type d \( -path ./benchmarks -o -path ./test/lib -o -path ./lib -o -path ./adam_src -o -path ./dependencies -o -path ./.git \) -prune -o -name '*' -not -regex ".*\(class\|qcir\|pdf\|tex\|apt\|dot\|jar\|ods\|txt\|tar.gz\|aux\|log\|res\|aig\|aag\|lola\|cex\|properties\|json\|xml\|out\|pnml\|so\)" -type f); do \
		echo "cp" $$i; \
		cp --parent $$i ./adam_src/ ;\
	done
	tar -zcvf adam_src.tar.gz adam_src
	rm -r -f ./adam_src
endef

# targets
all: tools ui

check_dependencies:
	@if [ ! -d "dependencies" ]; then \
		echo "The dependencies folder is missing. Please execute make pull_dependencies first.";\
	fi

pull_dependencies:
	./pull_dependencies.sh ${DEPENDENCIES_FOLDERS} ${DEPENDENCIES_REPOS} ${DEPENDENCIES_REV}

rm_dependencies:
	$(RM) -rf dependencies

tools: check_dependencies
	ant -buildfile ./dependencies/framework/tools/build.xml $(t)

ui: check_dependencies
	ant -buildfile ./build.xml $(t)

setJavac:
	$(eval t=javac)

setClean:
	$(eval t=clean)

setCleanAll:
	$(eval t=clean-all)

clean: setClean tools ui
	$(RM) -r -f deploy
	$(RM) -r -f javadoc

clean-all: setCleanAll tools ui
	$(RM) -r -f deploy
	$(RM) -r -f javadoc

#javadoc:
#	ant javadoc

src_withlibs: clean-all
	$(call generate_src, true)

src: clean-all
	$(call generate_src, false)
