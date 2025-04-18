package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * A Book's collection of Metadata.
 * In the future it should contain all Dublin Core attributes, for now
 * it contains a set of often-used ones.
 *
 * @author paul
 */
public class Metadata implements Serializable {

    private static final long serialVersionUID = -2437262888962149444L;

    public static final String DEFAULT_LANGUAGE = "en";

    private boolean autoGeneratedId;//true;
    private List<Author> authors = new ArrayList<>();
    private List<Author> contributors = new ArrayList<>();
    private List<Date> dates = new ArrayList<>();
    private String language = DEFAULT_LANGUAGE;
    private Map<QName, String> otherProperties = new HashMap<>();
    private List<String> rights = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<Identifier> identifiers = new ArrayList<>();
    private List<String> subjects = new ArrayList<>();
    private String format = MediaTypes.EPUB.getName();
    private List<String> types = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> publishers = new ArrayList<>();
    private Map<String, String> metaAttributes = new HashMap<>();

    public Metadata() {
        identifiers.add(new Identifier());
        autoGeneratedId = true;
    }

    @SuppressWarnings("unused")
    public boolean isAutoGeneratedId() {
        return autoGeneratedId;
    }

    /**
     * Metadata properties not hard-coded like the author, title, etc.
     *
     * @return Metadata properties not hard-coded like the author, title, etc.
     */
    public Map<QName, String> getOtherProperties() {
        return otherProperties;
    }

    public void setOtherProperties(Map<QName, String> otherProperties) {
        this.otherProperties = otherProperties;
    }

    @SuppressWarnings("unused")
    public Date addDate(Date date) {
        this.dates.add(date);
        return date;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Author addAuthor(Author author) {
        authors.add(author);
        return author;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Author addContributor(Author contributor) {
        contributors.add(contributor);
        return contributor;
    }

    public List<Author> getContributors() {
        return contributors;
    }

    public void setContributors(List<Author> contributors) {
        this.contributors = contributors;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public void setRights(List<String> rights) {
        this.rights = rights;
    }

    public List<String> getRights() {
        return rights;
    }


    /**
     * Gets the first non-blank title of the book.
     * Will return "" if no title found.
     *
     * @return the first non-blank title of the book.
     */
    public String getFirstTitle() {
        if (titles == null || titles.isEmpty()) {
            return "";
        }
        for (String title : titles) {
            if (StringUtil.isNotBlank(title)) {
                return title;
            }
        }
        return "";
    }

    public String addTitle(String title) {
        this.titles.add(title);
        return title;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getTitles() {
        return titles;
    }

    @SuppressWarnings("UnusedReturnValue")
    public String addPublisher(String publisher) {
        this.publishers.add(publisher);
        return publisher;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    @SuppressWarnings("UnusedReturnValue")
    public String addDescription(String description) {
        this.descriptions.add(description);
        return description;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    @SuppressWarnings("unused")
    public Identifier addIdentifier(Identifier identifier) {
        if (autoGeneratedId && (!(identifiers.isEmpty()))) {
            identifiers.set(0, identifier);
        } else {
            identifiers.add(identifier);
        }
        autoGeneratedId = false;
        return identifier;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
        autoGeneratedId = false;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @SuppressWarnings("UnusedReturnValue")
    public String addType(String type) {
        this.types.add(type);
        return type;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @SuppressWarnings("unused")
    public String getMetaAttribute(String name) {
        return metaAttributes.get(name);
    }

    public void setMetaAttributes(Map<String, String> metaAttributes) {
        this.metaAttributes = metaAttributes;
    }
}
